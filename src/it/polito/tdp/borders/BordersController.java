package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BordersController {
	
	Model model = new Model();
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtAnno;

    @FXML
    private Button btnCalcolaConfini;

    @FXML
    private ComboBox<Country> comboStati;

    @FXML
    private Button btnTrovaVicini;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaConfini(ActionEvent event) {
	txtResult.clear();
    	int anno;
    	
    	try {
    		anno = Integer.parseInt(txtAnno.getText());
    		
    		if( (anno < 1816) || (anno > 2006) ) {
        		txtResult.setText("Inserire un anno tra 1816 e 2006 !");
    			return;
    		}
        	
    	} catch(NumberFormatException ne) {
    		txtResult.setText("Inserire un anno !");
    		return;
    	}
    
    	
    	try {
        	model.createGraph(anno);
        	Map<Country, Integer> countryCounts = model.countryCounts();
        	// Stampo numero componenti connesse
        	txtResult.appendText(String.format("Numero componenti connesse: %d\n", model.getNumeroComponentiConnesse()));
        	
        	// Stampo ogni stato e per ciascuno il numero di stati confinanti
        	for(Country c : countryCounts.keySet()) {
        		txtResult.appendText(c.toString()+" " + countryCounts.get(c)+"\n");
        	}
        	comboStati.setDisable(false);
        	btnTrovaVicini.setDisable(false);
        	comboStati.getItems().addAll(model.getCountries());
        	
    	} catch(RuntimeException re) {
    		txtResult.setText(re.getMessage());
    	}
    }

    @FXML
    void doTrovaVicini(ActionEvent event) {
    	txtResult.clear();
    	Country country = comboStati.getValue();
    	List<Country> vicini = model.trovaVicini(country);
    	for(Country c : vicini)
    		txtResult.appendText(c + "\n");
    }

    @FXML
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Borders.fxml'.";
        assert btnCalcolaConfini != null : "fx:id=\"btnCalcolaConfini\" was not injected: check your FXML file 'Borders.fxml'.";
        assert comboStati != null : "fx:id=\"comboStati\" was not injected: check your FXML file 'Borders.fxml'.";
        assert btnTrovaVicini != null : "fx:id=\"btnTrovaVicini\" was not injected: check your FXML file 'Borders.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Borders.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
