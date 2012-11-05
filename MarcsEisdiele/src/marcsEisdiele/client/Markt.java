package marcsEisdiele.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Slider;

public class Markt extends Composite implements  HasText {

	private static MarktUiBinder uiBinder = GWT.create(MarktUiBinder.class);

	interface MarktUiBinder extends UiBinder<Widget, Markt> {
	} 

	public Markt() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField Button bPersonal;
	@UiField Button bResearch;
	@UiField Button bMarketing;
	@UiField Button bMaschine;
	@UiField Button bCompetitor;
	@UiField Button bStart;
	@UiField Slider sWorkload;
	@UiField Slider sPrice;

	ContentPanel panel = new ContentPanel();
	VerticalPanel vPanel = new VerticalPanel();
	HorizontalPanel hPanel = new HorizontalPanel();

	public Markt(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		bPersonal.setText(firstName);
	}

	@UiHandler("bPersonal")
	void onClickPersonal(ClickEvent e) {	
//		PopupWindow pPersonal = new PopupWindow("Personalverwaltung");
	}
	
	@UiHandler("bStart")
	void onClickStart(ClickEvent e) {
//		Auswertung[] auswertung = null;
//		auswertung[0] = new Auswertung();
//	    panel.add(auswertung[0].getAuswertung());
//		panel.remove(0);
	}

	public void setText(String text) {
		bPersonal.setText(text);
	}

	public String getText() {
		return bPersonal.getText();
	}
	
	public Widget getMarkt(){
		bPersonal.setText("Personal einstellen/ entlassen");
		bMarketing.setText("in Marketing investieren");
		bResearch.setText("in Forschung investieren");
		bMaschine.setText("Maschine hinzufuegen");
		bCompetitor.setText("Konkurrenzverhalten bearbeiten");
		bStart.setText("Markt starten");

		return this;
	}
}