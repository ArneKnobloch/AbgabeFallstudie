package marcsEisdiele.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Klasse schmei�t Exception beim schlie�en vom Fenster
 * dazu folgendes:
 * Turns out that client Java code (which is complied to Javascript) had try/catch block which worked when executed in Java, but failed silently when compiled to Javascript.
 * **/

public class PopupWindow implements IsWidget {
    String header;
    String bezeichnung;
    int wert;
    IntegerBox bWertAlt = new IntegerBox();
    IntegerBox bWertNeu = new IntegerBox();
    
	PopupWindow(String header, String bezeichnung, int wert){
		this.header = header;
		this.bezeichnung = bezeichnung;
		this.wert = wert;
		RootPanel.get().add(asWidget());
	}
	private VerticalPanel vp = null;
	  @Override
	  public Widget asWidget() {
		if (vp == null) {
	      final Window window = new Window();
	      window.setPixelSize(400, 300);
	      window.setModal(true);
	      window.setBlinkModal(true);

	      window.setHeadingText(header);
	      window.addHideHandler(new HideHandler() {
	        @Override
	        public void onHide(HideEvent event) {
	          TextButton open = ((Window) event.getSource()).getData("open");
	          try {
		          open.focus();
			} catch (Exception e) {
				// TODO: handle exception
			}

	        }
	      });
	 
//	      TabPanel panel = new TabPanel();
//	      panel.setBorders(false);
	      VerticalPanel vPanel = new VerticalPanel();

	      vPanel.setStyleName("popup"); 
	      
	      HorizontalPanel hPanel1 = new HorizontalPanel();
	      Label label1 = new Label(bezeichnung + " alt");
	      label1.addStyleName("label_popup");
	      bWertAlt.setValue(wert);
	      bWertAlt.setStyleName("textbox_popup");
	      hPanel1.add(label1);
	      hPanel1.add(bWertAlt);
	     
	      vPanel.add(hPanel1);
	      
	      HorizontalPanel hPanelImg = new HorizontalPanel();
	      Image imgPlus = new Image("images/plus.png");
	      imgPlus.setSize("20px", "20px");
	      Button buttonPlus = new Button();
	      buttonPlus.setStyleName("img_popup");
	      buttonPlus.getElement().appendChild(imgPlus.getElement());
	      hPanelImg.add(buttonPlus);
	      
	      Image imgMinus = new Image("images/minus.png");
	      imgMinus.setSize("20px", "20px");
	      Button buttonMinus = new Button();
	      buttonMinus.setStyleName("img_popup");
	      buttonMinus.getElement().appendChild(imgMinus.getElement());
	      hPanelImg.add(buttonMinus);
	      vPanel.add(hPanelImg);
	      
	      HorizontalPanel hPanel2 = new HorizontalPanel();
	      Label label2 = new Label(bezeichnung + " neu");
	      label2.addStyleName("label_popup");
	      bWertNeu.setValue(wert);
	      bWertNeu.setStyleName("textbox_popup");
	      hPanel2.add(label2);
	      hPanel2.add(bWertNeu);
	      vPanel.add(hPanel2);
	 
//	      panel.add(label1, new TabItemConfig("Personal einstellen"));
//	      panel.add(label2, new TabItemConfig("Personal entlassen"));
	 
	      window.add(vPanel);
	      
	      buttonPlus.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				wert++;
			    bWertNeu.setValue(wert);
			    System.out.print(wert);
			}
		});
	      buttonMinus.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				wert--;
			    bWertNeu.setValue(wert);	
			    System.out.print(wert);
			}
		}); 
	      
	      TextButton bSpeichern = new TextButton("Speichern");
	      bSpeichern.addSelectHandler(new SelectHandler() {
	 
	        @Override
	        public void onSelect(SelectEvent event) {
		  		  wert = bWertNeu.getValue();
	          window.hide();
	        }
	      });
	      
	      TextButton bSchliessen = new TextButton("Schliessen");
	      bSchliessen.addSelectHandler(new SelectHandler() {
	        @Override
	        public void onSelect(SelectEvent event) {
	  		  wert = bWertAlt.getValue();
	          window.hide();
	        }
	      });
	      window.addButton(bSchliessen);
	      window.addButton(bSpeichern);
	      window.setFocusWidget(window.getButtonBar().getWidget(0));

	      window.show();
	      vp = new VerticalPanel();
		}
		return vp;
	  }
	  public int getWert(){

		  return wert;
	  }

	  
	}