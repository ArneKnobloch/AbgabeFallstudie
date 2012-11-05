package marcsEisdiele.server;

import java.util.List;
import java.util.Map;
import java.util.Set;

import marcsEisdiele.shared.Unternehmen;


public class API_4_evaluation {
	static int market = 500000;
	private static int marketBehaviour(){
		market = (int) (market * API_4_calculation.marketBehavior);
		return market;
	}

	public static void startEvaluation(final List<Unternehmen> alleUnternehmenRunde) {
		marketBehaviour();
		Unternehmen un;
		for (int i= 0;i< alleUnternehmenRunde.size();i++){
			un = (Unternehmen) alleUnternehmenRunde.get(i);
			newSoldProducts(un);
			newExpenses(un);			
			newVerfuegbareGE(un);

		}
		newMarketShare(alleUnternehmenRunde);
	}
	
	
	private static void newMarketShare(final List<Unternehmen> alleUnternehmenRunde) {
		
		Unternehmen un;
		for (int i= 0;i< alleUnternehmenRunde.size();i++){
			un = (Unternehmen) alleUnternehmenRunde.get(i);
//			System.out.println("Unternehmen" +un);
			un.setMarketShare( (int)( un.getSoldProducts()*100 / market));
		}	
		
	}
	private static void newExpenses(Unternehmen un ) {
		//Berechnung aller Ausgaben
		//Fixkostenberechnung, variable Kosten
		un.setTotalExpenditure( (int) (un.getFixCost() + un.getVarCosts() * un.getProducedProducts() + un.getMarketing()));		
	}


	private static void newVerfuegbareGE(Unternehmen un) {
		// Neuberechnung des Kapitals und des Gewinns
		// um Faktoren wie Verbrauch der Maschinen und die damit verbundene Neubeschaffung, sowie unvorhersehbare Schadensf�lle,
		// R�ckerstattungen, Unf�lle, Krankheitsf�lle, etc. zu ber�cksichtigen wird der Kapital "zuf�llig" gemindert
		// beeinflussende Fakoteren : Personal (Krankheitsfall, Schwangerschaft, Ausfall), Maschinen (Ersatz einer Maschine, Reperatur, Wartung), Maschinenkapazit�t (Ausfall einer Maschine),
		int kapitalOld = un.getKapital();
		int kapitalNew = (int) ( kapitalOld - ( 0.05 * un.getPersonal() * 2000 +
				(Math.random() * (un.getMachines() - 1.0) + 1.0) * un.getMachines() * 4000 + (Math.random() * 0.4 ) * un.getMachinesCapacity() ) );
		un.setKapital( kapitalNew );
		un.setGewinn( un.getProductprize() * un.getSoldProducts() - un.getTotalExpenditure() );
	}


	private static void newSoldProducts(Unternehmen un) {
		//Neuberechnung der verkauften Produkte
		
		// m�gliche verkaufte Menge :
		// => Gesamtnachfrage des Marktes * Marktbindung
		// Marktbindung = ( ( Marketing * Marketinggewichtung + Qualit�t * Qualit�tsgewichtung ) / ( Preis * Preisgewichtung ) ) / max. Marktbindung
		// max. Marktbindung bedeutet Erreichung der theoretisch optimalen Werte Preis = 10; Marketing 50000; Qualit�t = 10
		// tats�chlich verkaufte Menge => m�gliche verkaufte Menge - ( m�gliche verkaufte Menge - vorhandene Menge)
		// vorhandene Menge => produzierte Menge + Lager
		// produzierte Menge => Maschinenkapazit�t * Maschinenauslastung * Produktivit�t / 100
		// Lager = Lager
		
		int store = un.getUsedStorage();
		// Es k�nnen nicht mehr Produkte gefertigt werden, als die Kapazit�ten der Maschinen, gemessen an ihrer geplanten Auslastung.
		// Es k�nnen ebenfalls nicht mehr Produkte gefertigt werden, als die Arbeiter in der Lage sind pro Stunde zu fertigen.
		// (Die Stunden f�r einen Mitarbeiter werden �ber ein Quartal gerechnet, mit einer zu rechnenden Stundenzahl von 133h im Monat)
		// folglich:
		int maximumMachine = (int) (un.getMachinesCapacity() * un.getMachineWorkload() / 100);
//		System.out.println("maximum Machine : " + maximumMachine);
		int maximumPersonal = (int) (un.getPersonal() * un.getProductivity() * 133 * 3 / 100);
//		System.out.println("maximum Personal : " + maximumPersonal);
		int producedProducts = 0;
		
		if (maximumPersonal >= maximumMachine)
			producedProducts = maximumMachine;
		else
			producedProducts = maximumPersonal;
		
		int availibleVolume = producedProducts + store;
//		System.out.println(" zu verkaufende Produkte : " + availibleVolume);
		un.setProducedProducts( producedProducts );
		
		int marketingemphasis = 3;
		int qualityemphasis = 50000;
		int prizeemphasis = 65000;
		double marketing = un.getMarketing() * marketingemphasis;
		double quality = un.getQuality() * qualityemphasis;
		double prize = un.getProductprize() * prizeemphasis;
		
		int opportunityOfSoldProducts = (int)  (market * ( marketing + quality) / (prize * 1.2) );
		
//		System.out.println("m�gliche verkaufte Produkte : " + opportunityOfSoldProducts);
		if (opportunityOfSoldProducts >= availibleVolume)
		{
			un.setUsedStorage(0);
			un.setSoldProducts(availibleVolume);
		}
		else{	//opportunityOfSoldProducts < availibleVolume
				un.setSoldProducts(opportunityOfSoldProducts);
				//pr�fen ob unverkaufte Produkte ins Lager passen, ansonsten werden Strafzahlungen f�llig
				//Strafzahlungen stellen hierbei den Verlust der Ware und der damit investierten Produktionsmittel etc. dar
				if( ( availibleVolume - opportunityOfSoldProducts) <= un.getStorage())
					un.setUsedStorage( availibleVolume - opportunityOfSoldProducts);
				else{
					un.setUsedStorage( un.getStorage() );
					//Strafzahlung
					un.setKapital(un.getKapital() - (availibleVolume - opportunityOfSoldProducts) * un.getProductprize() * 2 );
				}
		}
	}


}