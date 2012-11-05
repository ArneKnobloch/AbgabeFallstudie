package marcsEisdiele.client;

import java.util.List;

import marcsEisdiele.server.API_4_calculation;
import marcsEisdiele.server.API_4_evaluation;
import marcsEisdiele.shared.Unternehmen;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.IntegerBox;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.TabPanel;

/*
 * Für hannah :-)
 * Das sind soweit die implementierungen der Methoden. Ich werde mich heute vermutlich noch dran setzen, dass man
 * Unternehmen überschreiben kann, bzw dass man eine liste an unternehmen speichern/überschreiben kann
 * Alle Unternehmen auslesen:
  service.getAlleUnternehmen(new AsyncCallback<List<Unternehmen>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(List<Unternehmen> result) {
				alleUnternehmen = result;				
			}
			
		});
		
 *Alle Unternehmen einer Runde auslesen:
		service.getALleUnternehmenRunde(runde,new AsyncCallback<List<Unternehmen>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(List<Unternehmen> result) {
				alleUnternehmenRunde = result;				
			}
			
		});
 *Ein Unternehmen speichern:
		service.addUnternehmen(unternehmen,new AsyncCallback<void>(){

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Unternehmen result) {			
			}
			
		});
 * 
 */

public class Runde extends Composite implements  HasText {

	private static RundeUiBinder uiBinder = GWT.create(RundeUiBinder.class);
	private UnternehmensServiceAsync service;
	private Unternehmen unternehmen, unternehmen1, unternehmen2, unternehmen3;
	private int runde, marktentwicklung;
	private List<Unternehmen> alleUnternehmenRunde;
	public boolean oldGame;

	interface RundeUiBinder extends UiBinder<Widget, Runde> {
	} 

	public Runde(int nRunde) {
		runde = nRunde;
		
		
	}
	
	@UiField Button bPersonal;
	@UiField Button bMaschine;
	@UiField Slider sResearch;
	@UiField IntegerBox currentResearch;
	@UiField Slider sMarketing;
	@UiField IntegerBox currentMarketing;
	@UiField Slider sWorkload;
	@UiField IntegerBox currentWorkload;
	@UiField Slider sPrice;
	@UiField IntegerBox currentPrice;
	@UiField Button bCompetitor;
	@UiField Button bStart;
	@UiField RadioButton rbWenig;
	@UiField RadioButton rbMittel;
	@UiField RadioButton rbViel;
	@UiField Label lUnName;
	@UiField Label lKapital;
	@UiField Label lPersonal;
	@UiField Label lLager;
	

	TabPanel mainPanel = new TabPanel();

	ContentPanel panel = new ContentPanel();
	VerticalPanel vPanel = new VerticalPanel();
	HorizontalPanel hPanel = new HorizontalPanel();
	int minMarketing = 10000, maxMarketing = 100000, marketing;
	int minResearch = 50000, maxResearch = 150000, research;
/////////////////////////////////////////////////////////////////////////////////////////
	int minWorkload = 0, maxWorkload = 100, workload; //Workload ist Auslastung der Maschinen! ??? Warum war er über 800 , andere Interpreation von Workload ??
	int minPrice = 10, maxPrice = 200, price;
	int stratUn1 = 7, stratUn2 = 7, stratUn3 = 7; 
	public Runde(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		bPersonal.setText(firstName);
	}
	
	
	int personal = 0; //Abfrage des Objektes
	@UiHandler("bPersonal")
	void onClickPersonal(ClickEvent e) {	
		PopupWindow pPersonal = new PopupWindow("Personalverwaltung", "Personal",personal);
		if (pPersonal.getWert()>0) personal = pPersonal.getWert();
	}
	int maschine = 0; //Abfrage des Objektes
	@UiHandler("bMaschine")
	void onClickMaschine(ClickEvent e) {	
		PopupWindow pMaschine = new PopupWindow("Maschinen verwalten", "Maschinenanzahl",maschine);
		if(pMaschine.getWert()>0) maschine = pMaschine.getWert();
	}
    @UiHandler( { "rbWenig", "rbMittel", "rbViel" })
    void onMarktentwicklung(ValueChangeEvent<Boolean> e) {
    	if (rbWenig.getValue()== true) marktentwicklung = 1;
    	if (rbMittel.getValue()== true) marktentwicklung = 2;
    	if (rbViel.getValue()== true) marktentwicklung = 3;
//    	System.out.println("Marktentwicklung = " + marktentwicklung);
    }
	@UiHandler("bCompetitor")
	void onClickCompetitor(ClickEvent e) {	
		PopupWindowCompetitor pCompetitor = new PopupWindowCompetitor("Konkurrenzverhalten bestimmen", unternehmen1.getNameUN(), unternehmen2.getNameUN(), unternehmen3.getNameUN());
		stratUn1 = pCompetitor.getStrategie1();
		stratUn2 = pCompetitor.getStrategie2();
		stratUn3 = pCompetitor.getStrategie3();
		System.out.println("Hallo " + stratUn1 + stratUn2 + stratUn3);
	}
	
	@UiHandler("bStart")	
	void onClickStart(ClickEvent e) {
		/***eigenes Objekt berechnen***/
		API_4_calculation.personalAenderung(unternehmen,  personal - unternehmen.getPersonal());
		API_4_calculation.maschineKaufen(unternehmen, maschine - unternehmen.getMachines());
		API_4_calculation.forschungsInvestition(unternehmen, research);
		API_4_calculation.marketingInvestition(unternehmen, marketing);
		API_4_calculation.marketBehavior(marktentwicklung);
		
		/***Strategien der Konkurrenz***/ 
//		API_4_calculation.competitorStrategie(/**einzelnes unternehmen übergeben**/, /**Welche Strategie wurde für welches UN geweählt - Methode ?!**/)
//		Bsp:
		if ((stratUn1 == 7) ^ (stratUn2 == 7) ^ (stratUn3 == 7)){
			stratUn1 = Random.nextInt(6);
			stratUn2 = Random.nextInt(6);
			stratUn3 = Random.nextInt(6);
		}
			
		API_4_calculation.competitorStrategie(unternehmen1, stratUn1);
		API_4_calculation.competitorStrategie(unternehmen2, stratUn2);
		API_4_calculation.competitorStrategie(unternehmen2, stratUn3);
		/***Auswertung berechnen***/
		//->erstellen eines Sets aller Unternehmen!
		
		API_4_evaluation.startEvaluation(alleUnternehmenRunde);
	
		sMarketing.setEnabled(false);
		sPrice.setEnabled(false);
		sResearch.setEnabled(false);
		sWorkload.setEnabled(false);
		bCompetitor.setEnabled(false);
		bMaschine.setEnabled(false);
		bPersonal.setEnabled(false);
		bStart.setEnabled(false);

		unternehmen.setRound(runde);
		unternehmen1.setRound(runde);
		unternehmen2.setRound(runde);
		unternehmen3.setRound(runde);
//		unternehmen.setSoldProducts(10+runde);
//		unternehmen1.setSoldProducts(20+runde);
//		unternehmen2.setSoldProducts(30+runde);
//		unternehmen3.setSoldProducts(40+runde);
		
		alleUnternehmenRunde.add(unternehmen);
		alleUnternehmenRunde.add(unternehmen1);
		alleUnternehmenRunde.add(unternehmen2);
		alleUnternehmenRunde.add(unternehmen3);

		service.addAllUnternehmen(alleUnternehmenRunde,new AsyncCallback<Void>(){
		@Override
		public void onFailure(Throwable caught){
			
		}
		@Override
		public void onSuccess(Void result) {
			start.getAuswertung();
		}
			
		});
		
	}
	
	public void fakeClickstart(){
		bStart.setEnabled(false);
		sMarketing.setEnabled(false);
		sPrice.setEnabled(false);
		sResearch.setEnabled(false);
		sWorkload.setEnabled(false);
		start.getAuswertung();
	}

	public void setText(String text) {
		bPersonal.setText(text);
	}

	public String getText() {
		return bPersonal.getText();
	}
	
	public Widget getRunde(){
		initWidget(uiBinder.createAndBindUi(this));
		 //Alle Unternehmen einer Runde auslesen:
		service = GWT.create(UnternehmensService.class);
		service.getAlleUnternehmenRunde(start.zaehlerRunde-1,new AsyncCallback<List<Unternehmen>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(List<Unternehmen> result) {
				alleUnternehmenRunde = result;
				unternehmen = (Unternehmen) alleUnternehmenRunde.get(0);
				unternehmen1 = (Unternehmen) alleUnternehmenRunde.get(1);
				unternehmen2 = (Unternehmen) alleUnternehmenRunde.get(2);
				unternehmen3 = (Unternehmen) alleUnternehmenRunde.get(3);
//				alleUnternehmenRunde.clear();

				bPersonal.setText("Personal einstellen/ entlassen");
				bMaschine.setText("Maschinen verwalten");
				
				personal = unternehmen.getPersonal();
				maschine = unternehmen.getMachines();
				
				marketing = unternehmen.getMarketing();
				sMarketing.setMinValue(minMarketing);
				sMarketing.setMaxValue(maxMarketing);
				sMarketing.setIncrement(10); //steht increment für den Betrag einer Mindesteinheit wenn ja, dann -->1000 
				sMarketing.addValueChangeHandler(new ValueChangeHandler<Integer>() {
					public void onValueChange(ValueChangeEvent<Integer> event) {
						marketing = event.getValue(); 
						currentMarketing.setValue(marketing);
					}
				});
				currentMarketing.setValue((minMarketing+maxMarketing)/2);
				currentMarketing.addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent event) {
						marketing = currentMarketing.getValue();
						if(marketing<minMarketing) marketing = minMarketing;
						if(marketing>maxMarketing) marketing = maxMarketing;
			            sMarketing.setValue(marketing);
					}
				});
				
				
				sResearch.setMinValue(minResearch);
				sResearch.setMaxValue(maxResearch);
				sResearch.setIncrement(10); //--> 5000
				sResearch.addValueChangeHandler(new ValueChangeHandler<Integer>() {
					public void onValueChange(ValueChangeEvent<Integer> event) {
						research = event.getValue(); 
						currentResearch.setValue(research);
					}
				});
				currentResearch.setValue((minResearch+maxResearch)/2);
				currentResearch.addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent event) {
						research = currentResearch.getValue();
						if(research<minResearch) research =minResearch;
						if(research>maxResearch) research=maxResearch;
			            sResearch.setValue(research);
					}
				});
				
				sWorkload.setMinValue(minWorkload);
				sWorkload.setMaxValue(maxWorkload);
				sWorkload.setIncrement(1); //--> 5
				sWorkload.addValueChangeHandler(new ValueChangeHandler<Integer>() {
					public void onValueChange(ValueChangeEvent<Integer> event) {
						workload = event.getValue(); 
						currentWorkload.setValue(workload);
					}
				});
				currentWorkload.setValue((minWorkload+maxWorkload)/2);
				currentWorkload.addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent event) {
						workload = currentWorkload.getValue();
						if(workload<minWorkload) workload = minWorkload;
						if(workload>maxWorkload) workload = maxWorkload;
			            sWorkload.setValue(workload);
					}
				});
				
				sPrice.setMinValue(minPrice);
				sPrice.setMaxValue(maxPrice);
				sPrice.setIncrement(1); //--> 1
				sPrice.addValueChangeHandler(new ValueChangeHandler<Integer>() {
					public void onValueChange(ValueChangeEvent<Integer> event) {
						price = event.getValue(); 
						currentPrice.setValue(price);
					}
				});
				currentPrice.setValue((minPrice+maxPrice)/2);
				currentPrice.addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent event) {
						price = currentPrice.getValue();
						if(price<minPrice) price = minPrice;
						if(price>maxPrice) price = maxPrice;
			            sPrice.setValue(price);
					}
				});
				
				bCompetitor.setText("Konkurrenzverhalten bearbeiten");
				bStart.setText("Runde starten");
				setBox();
			}
			
		});
		
		return this;
	}
	void setBox(){

		lUnName.setText(unternehmen.getNameUN());
    	Integer inte = new Integer(unternehmen.getKapital());
		String s = inte.toString();
		lKapital.setText(s);
		inte = unternehmen.getPersonal();
		s = inte.toString();
		lPersonal.setText(s);
		inte = unternehmen.getStorage();
		s = inte.toString();
		lLager.setText(s);

    }
	
}