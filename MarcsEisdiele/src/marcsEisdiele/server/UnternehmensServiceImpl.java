package marcsEisdiele.server;

import java.lang.reflect.Array;
import java.util.List;

import marcsEisdiele.client.Game;
import marcsEisdiele.client.UnternehmensService;
import marcsEisdiele.client.UnternehmensServiceAsync;
import marcsEisdiele.shared.Unternehmen;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/*
 * Hier sind alle Methoden aufgelistet, die einen Zugriff auf die Datenbank ermöglichen
 * Die Kommentare darüber dürften die Anwendung spezifischer Methoden bzw. deren Sinn erkenntlich machen
 * Allgemein implementiert man diese Methoden folgendermaßen:
 * 1. Sicherstellen, dass eine Instanz des Service Objektes vorhanden ist
 * 		private UnternehmensServiceAsync service;		 <-- deklarieren
 * 		service = GWT.create(UnternehmensService.class); <--Initialisieren
 * 2. service.<methode(Parameter a, Parameter b, <nameDerMethodeCallback>)
 * 		z.b. service.addUnternehmen(Unternehmen u, addUnternehmenCallback);
 * 3. Sicherstellen, dass der Callback implementiert ist
 * 		in der selben Klasse wenn nicht vorhanden einen erstellen:
 * 		z.b.:
 * public class AddUnternehmenCallback implements AsyncCallback<java.lang.Void> {

        @Override
        public void onFailure(Throwable caught) {
        Info.display("Speicherung Fehlgeschlagen", "Beim Speichern der Daten in die Datenbank ist ein Fehler aufgetreten");
        }

        @Override
        public void onSuccess(Void result) {
        	Info.display("Speicherung abgeschlossen", "Die Daten wurden erfolgreich in die Datenbank geschrieben");
        }
    }
    	einfach kopieren, namen ändern und entsprechende bei success/failure das nötige eintragen.
    	
    	
    	OPTIONAL: mir sagen wo und was und ich machs :-)
 */


public class UnternehmensServiceImpl
	extends RemoteServiceServlet
	implements UnternehmensService{

	private static final long serialVersionUID = 3958578023704542908L;
	
	//Methode zum speichern eines Unternehmens in der Datenbank
	@Override
	public void addUnternehmen(Unternehmen unternehmen) {
		PersistenceManager.getDatastore().save(unternehmen);
		
	}
	
	// Methode die ein Unternehmen für eine bestimmte Runde zurückliefert
	// zu Übergeben: Name des Unternehmens, rundenzahl(sollte man aus der GUI lesen können)
	@Override
	public Unternehmen getUnternehmen(int id, int runde) {
		Datastore ds = PersistenceManager.getDatastore();
		Query<Unternehmen> q = ds.find(
                Unternehmen.class).filter("UNID =", id).filter("round =", runde).filter("gameID =", Game.gameID);
		return q.get();
		
	}
	//Methode die alle Unternehmen als Liste ausgibt
	@Override
	public List<Unternehmen> getAlleUnternehmen() {
		Datastore ds = PersistenceManager.getDatastore();
		return ds.createQuery(Unternehmen.class).filter("gameID =", Game.gameID).asList();
	}
	// Methode die alle Unternehmen für eine spezielle Runde ausgibt
	// zu Übergeben: Die Runde für die die Unternehmen ausgegeben werden soll
	@Override
	public List<Unternehmen> getAlleUnternehmenRunde(int runde) {
		Datastore ds = PersistenceManager.getDatastore();
		return ds.createQuery(Unternehmen.class).filter("round =", runde).filter("gameID =", Game.gameID).asList();
	}
	
	//Methode die für ein bestimmtes Unternehmen alle RundenObjekte ausgibt
	//zu Übergeben: Name des Unternehmens
	@Override
	public List<Unternehmen> getAlleSpezifisch(int UNID) {
		Datastore ds = PersistenceManager.getDatastore();
		return ds.createQuery(Unternehmen.class).filter("UNID =", UNID).filter("gameID =", Game.gameID).asList();
	}

	@Override
	public void addAllUnternehmen(List<Unternehmen> unternehmenAll) {
		for(int i = 0; i < unternehmenAll.size();i++){
			Unternehmen unternehmen = unternehmenAll.get(i);
			PersistenceManager.getDatastore().save(unternehmen);
		}
		
	}

	@Override
	public Unternehmen getGameID() {
		int[] gameID = new int[2];
		Datastore ds = PersistenceManager.getDatastore();
		Unternehmen unternehmen = ds.createQuery(Unternehmen.class).order("gameID").order("-round").limit(1).get();
		return unternehmen;
	}

}
