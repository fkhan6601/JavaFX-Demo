package application;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class uses javaFX to create a gui for the music DB.
 * It can load from and save to a file.It can add music, 
 * modify music, and delete music.
 * @author Farrukh Khan
 *
 */
public class SongDatabase extends Application
{
	//*****************************************************
	//Global Variables
	//*****************************************************
	public String passedParam;
	public ComboBox<String> musicTitle;
	public Button addAll;
	public Button modBut;
	public Button delBut;
	public Button confAddCanc;
	public Button errorBtn;
	public Scene scene1;
	public Scene scene2;
	public Scene scene3;
	public Label title;
	public Label artist;
	public Label desc;
	public Label album;
	public TextField itmAddTxt;
	public TextField titAddTxt;
	public TextField artAddTxt;
	public TextField yrAddTxt;
	public TextField albAddTxt;
	public Label itmModTxt;
	public TextField titModTxt;
	public TextField artModTxt;
	public TextField yrModTxt;
	public TextField albModTxt;
	public Map<String, MusicMeta> musicMap = new HashMap<String, MusicMeta>();
	
	//*****************************************************
	//Main Stage
	//*****************************************************
	/**
	 * This method creates the 3 scenes and all screen objects
	 * @param primaryStage- main stage for gui
	 */
	public void start(Stage primaryStage) throws Exception 
	{
		//Pass file location to method for creating Map
		passedParam = getParameters().getRaw().get(0);
		File musicFile = new File(passedParam);
		String fileStatus = MakeMap(musicFile);
		
		
		//********************************
		//Scene 1- Display
		//********************************
		
	    //create 4 pane objects for scene 1
	    VBox rootNode = new VBox(50);
	    
	    FlowPane top1 = new FlowPane(Orientation.HORIZONTAL, 50, 5);
	    
	    VBox mid1 = new VBox(10);
	    mid1.setBorder(new Border(new BorderStroke(Color.BLACK, 
	              BorderStrokeStyle.SOLID, CornerRadii.EMPTY, 
	              BorderWidths.DEFAULT)));
	    mid1.setStyle("-fx-background-color: white");;
	    
	    FlowPane bottom1 = new FlowPane(Orientation.HORIZONTAL, 5, 5);
	    bottom1.setAlignment(Pos.CENTER);
	    
	    //Creating a Scene by passing the group object, height and width   
	    scene1 = new Scene(rootNode ,350, 300); 
	    
	    //Create combobox
	    musicTitle = new ComboBox<String>(FXCollections.observableArrayList(
        		CreateCombo(musicMap)));
	    musicTitle.getSelectionModel().selectFirst();
	    
	    //Change text with combo box
	    musicTitle.valueProperty().addListener((obs, oldval, newval) -> {
	        if(newval != null)
	        {
	        	title.setText("Title: " + 
	        			musicMap.get(newval).getTitle());
	        	artist.setText("Artist: "+ 
	    	    		musicMap.get(newval).getArtist());
	        	desc.setText("Description: "+ 
	        			musicMap.get(newval).getDescription());
	        	album.setText("Album: "+ 
	        			musicMap.get(newval).getAlbum());
	        	
	        	itmModTxt.setText(
	        			musicMap.get(newval).getItemCode());
	        	titModTxt.setText(
	    	    		musicMap.get(newval).getTitle());
	        	artModTxt.setText(
	    	    		musicMap.get(newval).getArtist());
	        	yrModTxt.setText(
	        			musicMap.get(newval).getDescription());
	        	albModTxt.setText(
	        			musicMap.get(newval).getAlbum());
	        }});
	    
	    //Combo box title
	    Label comboLab = new Label("Title of Song:");
	    
	    //Display song info
	    title = new Label("Title: " 
	    		+ musicMap.get(musicTitle.getValue()).getTitle());
	    artist = new Label("Artist: "+ 
	    		musicMap.get(musicTitle.getValue()).getArtist());
	    
	    desc = new Label("Description: "+ 
	    		musicMap.get(musicTitle.getValue()).getDescription());
	    
	    album = new Label("Album: "+ 
	    		musicMap.get(musicTitle.getValue()).getAlbum());
	    
	    //Buttons for adding/deleting/modifying music
	    Button addBut = new Button("Add");
	    addBut.setOnAction(e -> primaryStage.setScene(scene2));
	    
	    modBut = new Button("Modify");
	    modBut.setOnAction(e -> primaryStage.setScene(scene3));
	    
	    delBut = new Button("Delete");
	    delBut.setOnAction(new ConfirmDelete());
	    
	    //Button to exit and save
	    Button extBut = new Button("Exit");
	    extBut.setOnAction(new ExitApp());
	    
	    //Add children to scene 1
	    top1.getChildren().addAll(comboLab, musicTitle);
	    mid1.getChildren().addAll(title, artist, desc, album);
	    bottom1.getChildren().addAll(addBut, modBut, delBut, extBut);
	    rootNode.getChildren().addAll(top1, mid1, bottom1);
	    
	    //********************************
	    //Scene 2- Add
	    //********************************
	    
	    //create pane object for scene 2
	    VBox addNode = new VBox(20);
	    scene2 = new Scene(addNode, 350, 300);
	    
	    //Item text and label
	    Label itmAddLab = new Label("Item Code: ");
	    itmAddTxt = new TextField();
	    HBox itmAddBx = new HBox(27);
	    itmAddBx.getChildren().addAll(itmAddLab, itmAddTxt);
	    
	    //Title text and label
	    Label titAddLab = new Label("Title: ");
	    titAddTxt = new TextField();
	    HBox titAddBx = new HBox(27);
	    titAddBx.getChildren().addAll(titAddLab, titAddTxt);
	    
	    //Artist text and label
	    Label artAddLab = new Label("Artist: ");
	    artAddTxt = new TextField();
	    HBox artAddBx = new HBox(20);
	    artAddBx.getChildren().addAll(artAddLab, artAddTxt);
	    
	    //Description text and label
	    Label yrAddLab = new Label("Description: ");
	    yrAddTxt = new TextField();
	    HBox yrAddBx = new HBox(24);
	    yrAddBx.getChildren().addAll(yrAddLab, yrAddTxt);
	    
	    //Album text and label
	    Label albAddLab = new Label("Album: ");
	    albAddTxt = new TextField();
	    HBox albAddBx = new HBox(13);
	    albAddBx.getChildren().addAll(albAddLab, albAddTxt);
	    
	    //Add button
	    Button addAll = new Button("Add");
	    addAll.setOnAction(new ConfirmAdd());
	    
	    //Back button
	    Button cancAdd = new Button("Back");
	    cancAdd.setOnAction(e -> primaryStage.setScene(scene1));
	    
	    //Add all children to scene 2
	    HBox butAddBx = new HBox(40);
	    butAddBx.setAlignment(Pos.CENTER);
	    butAddBx.getChildren().addAll(addAll, cancAdd);
	    
	    addNode.getChildren().addAll(itmAddBx, titAddBx, artAddBx, 
	    		yrAddBx, albAddBx, butAddBx);
	    
	    //********************************
	    //Scene 3- Modify
	    //********************************
	    
	    //create pane object for scene 3
	    VBox modNode = new VBox(20);
	    scene3 = new Scene(modNode, 350, 300);
	    
	    //Item text and label
	    Label itmModLab = new Label("Item Code: ");
	    itmModTxt = new Label();
	    HBox itmModBx = new HBox(27);
	    itmModBx.getChildren().addAll(itmModLab, itmModTxt);
	    
	    //Title text and label
	    Label titModLab = new Label("Title: ");
	    titModTxt = new TextField();
	    HBox titModBx = new HBox(27);
	    titModBx.getChildren().addAll(titModLab, titModTxt);
	    
	    //Artist text and label
	    Label artModLab = new Label("Artist: ");
	    artModTxt = new TextField();
	    HBox artModBx = new HBox(20);
	    artModBx.getChildren().addAll(artModLab, artModTxt);
	    
	    //Year text and label
	    Label yrModLab = new Label("Description: ");
	    yrModTxt = new TextField();
	    HBox yrModBx = new HBox(24);
	    yrModBx.getChildren().addAll(yrModLab, yrModTxt);
	    
	    //Album text and label
	    Label albModLab = new Label("Album: ");
	    albModTxt = new TextField();
	    HBox albModBx = new HBox(13);
	    albModBx.getChildren().addAll(albModLab, albModTxt);
	    
	    //Modify button
	    Button modAll = new Button("Modify");
	    modAll.setOnAction(new ConfirmModify());
	    
	    //Back button
	    Button cancMod = new Button("Back");
	    cancMod.setOnAction(e -> primaryStage.setScene(scene1));
	    
	    //Add all children to scene 3
	    HBox butModBx = new HBox(40);
	    butModBx.setAlignment(Pos.CENTER);
	    butModBx.getChildren().addAll(modAll, cancMod);
	    
	    modNode.getChildren().addAll(itmModBx, titModBx, artModBx, 
	    		yrModBx, albModBx, butModBx);
	    
	    //********************************
	    //Show Main Stage
	    //********************************
	    
	    //Set the title to stage. 
	    primaryStage.setTitle("Music Player"); 
	   
	    //Add the scene to stage 
	    primaryStage.setScene(scene1); 
	       
	    //Display the contents of the stage
	    primaryStage.show(); 
	 }
	
	//*****************************************************
	//Main Method
	//*****************************************************
	/**
	 * This method launches the Start method and accepts
	 * arguments from terminal.
	 * @param args- Args passed from console
	 */
	public static void main(String[] args)
	{
		//Create file to read
		File fileCheck = new File(args[0]);
		
		//Use Scanner to communicate with user
		Scanner userResp = new Scanner(System.in);
		
		//Check if file exist
		if(fileCheck.isFile() == false || fileCheck.exists() == false)
		{
			//Prompt user
			System.out.println("Song file does not exist. Press 1 to proceed. "
					+ "Press any other number to end.");
			
			//If user want to continue, go to app
			if(userResp.nextInt() == 1)
			{
				Application.launch(args);
			}
			
			//If user ends, end app
			else 
			{
				System.exit(0);
			}
		}
		
		//If file exists, go to app
		else
		{
			//Launch start method and pass args
			Application.launch(args);
		}
		
	}
	
	//*****************************************************
	//Confirmation Popup
	//*****************************************************
	/**
	 * This method creates the popup confirmation scene.
	 * It takes arguments for task to determine the correct
	 * prompt and method to execute.
	 * @param task- Int 1-3 for triggering add, modify, delete
	 */
	public void ConfAdd(int task)
	{
		//Create a new stage
		Stage confAddStage = new Stage();
		
		//Create a new pane and add to scene
		VBox confAddBox = new VBox(20);
		confAddBox.setAlignment(Pos.CENTER);
		Scene confAddScene = new Scene(confAddBox, 220, 80);
		
		//Create okay button
		Button confAddOk = new Button("OK");
		
		//Create cancel button
		confAddCanc = new Button("Cancel");
		confAddCanc.setOnAction(new CancelAdd());
		
		//Create a prompt label
		Label confAddLab = new Label(" ");
		
		//Determine which label and method to use
		if(task == 1)
		{
			//Label and method to add entry
			confAddLab.setText("Are you sure you want to add?");
			confAddOk.setOnAction(new SaveEntry());
		}
		
		if(task ==2)
		{
			//label and method to modify entry
			confAddLab.setText("Are you sure you want to modify?");
			confAddOk.setOnAction(new ModifyEntry());
		}
		
		if(task==3)
		{
			//Label and method to delete entry
			confAddLab.setText("Are you sure you want to delete?");
			confAddOk.setOnAction(new DeleteEntry());
		}
		
		//Add label and buttons to popup scene
		HBox confAddButBx = new HBox(20);
		confAddButBx.setAlignment(Pos.CENTER);
		confAddButBx.getChildren().addAll(confAddOk, confAddCanc);
		
		confAddBox.getChildren().addAll(confAddLab, confAddButBx);
		
		//Name and show new scene and disable main scene
		confAddStage.setTitle("Confirmation");
		confAddStage.setScene(confAddScene);
		confAddStage.initModality(Modality.APPLICATION_MODAL);
		confAddStage.showAndWait();
	}
	
	public void keyError()
	{
		//Create Stage
		Stage error = new Stage();
		
		//Add pane
		VBox errorBx = new VBox(20);
		errorBx.setAlignment(Pos.CENTER);
		
		//Add label and button
		Label errorLb = new Label("Item Code already exist!");
		errorBtn = new Button("Okay");
		errorBtn.setOnAction(new Cancel());
		
		//Add children to pane
		errorBx.getChildren().addAll(errorLb, errorBtn);
		
		//Add pane to scene
		Scene errorSc = new Scene(errorBx, 250, 100);
		
		//Show stage
		error.setTitle("Error!");
		error.setScene(errorSc);
		error.initModality(Modality.APPLICATION_MODAL);
		error.showAndWait();
	}
	
	//*****************************************************
	//Helper Methods
	//*****************************************************
	/**
	 * This method takes the key from the music Map and
	 * converts it to a list for the combobox.
	 * @param music- Map of string, MusicMeta object
	 * @return String List of music title
	 */
	public List<String> CreateCombo(Map<String, MusicMeta> music)
	{
		//Create a new list
		List<String> musicList = new ArrayList<String>();
		
		//Cycle through map and add key to list
		for(Map.Entry<String, MusicMeta> entry : music.entrySet())
		{
			musicList.add(entry.getKey());
		}
		
		//Return the list
		return musicList;
	}
	
	/**
	 * This method adds new entries to the map.
	 * It is executed when add is confirmed.
	 */
	public void AddEntryToMap()
	{
		//Create a new musicmeta object
		MusicMeta tempMusic = new MusicMeta();
		
		//If title is not blank add info to new object
		if(itmAddTxt.getText() != " " && itmAddTxt.getText() != "")
		{
			if(musicMap.containsKey(itmAddTxt.getText()) != true)
			{
				//Add info to new object
				tempMusic.setMusic(itmAddTxt.getText(), titAddTxt.getText(), 
						artAddTxt.getText(),
						yrAddTxt.getText(), albAddTxt.getText());
				
				//Add object to Map
				musicMap.put(itmAddTxt.getText(), tempMusic);
			}
			
			else
			{
				keyError();
			}
			
		}
		
		//Update the combobox
		musicTitle.setItems(FXCollections.observableArrayList(
        		CreateCombo(musicMap)));
	}
	
	/**
	 * This method deletes entries from the map.
	 * it is executed when delete is confirmed.
	 */
	public void DeleteEntryFromMap()
	{
		//If the entry is not the placeholder
		if(musicTitle.getValue() != " ")
		{
			//Delete the entry from Map
			musicMap.remove(musicTitle.getValue());
		}
		
		//Update combobox
		musicTitle.setItems(FXCollections.observableArrayList(
        		CreateCombo(musicMap)));
		
		//Set combobox to placeholder
		musicTitle.getSelectionModel().selectFirst();
	}
	
	/**
	 * This methods modifies current entries.
	 * It is executed after modify confirmation.
	 */
	public void ModifyEntryFromMap()
	{
		//Create a new MusicMeta object
		MusicMeta tempMusic = new MusicMeta();
		
		//If entry is not placeholder
		if(musicTitle.getValue() != " ")
		{
			//Remove old key from map
			musicMap.remove(musicTitle.getValue());
				
			//Add info to new object
			tempMusic.setMusic(itmModTxt.getText(), titModTxt.getText(), 
					artModTxt.getText(),
					yrModTxt.getText(), albModTxt.getText());
				
			//Add new object to Map
			musicMap.put(itmModTxt.getText(), tempMusic);
		}
		
		//Update combobox
		musicTitle.setItems(FXCollections.observableArrayList(
        		CreateCombo(musicMap)));
		
		//Select the placeholder in combobox
		musicTitle.getSelectionModel().selectFirst();
	}
	
	/**
	 * This method saves the info in the Map to
	 * the user specified file and exits the app.
	 */
	public void ExitSave()
	{
		//Use try and catch for file IO errors
		try 
		{
			//create a new writer to write to file
			Writer newFile = new FileWriter(passedParam, false);
			
			//Loop through Map entries
			for(Map.Entry<String, MusicMeta> entry : musicMap.entrySet())
			{
				//Save all but the placeholder
				if(entry.getValue().getTitle() != " ")
				{
					//Write info from Map
					newFile.write("ItemCode:" 
							+ entry.getValue().getItemCode() + "\n");
					newFile.write("Title:" 
							+ entry.getValue().getTitle() + "\n");
					newFile.write("Artist:" 
								+ entry.getValue().getArtist() + "\n");
					newFile.write("Description:" 
									+ entry.getValue().getDescription() + "\n");
					newFile.write("Album:" 
										+ entry.getValue().getAlbum() + "\n");
					newFile.write("" + "\n");
				}
			}
			
			//Close the file
			newFile.close();
			
			//Exit the program
			Platform.exit();
		}
		
		//Catch any exceptions
		catch (IOException e) 
		{
			//Communicate errors to user
			System.out.println("Error writing file. Check if file is open.");
		}
	}
	
	//*****************************************************
	//Event Handlers
	//*****************************************************
	/**
	 * This event handler handles the confirmation button in 
	 * the add scene.
	 * 
	 *
	 */
	public class SaveEntry implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			//Close the prompt
			Stage stage = (Stage) confAddCanc.getScene().getWindow();
		    stage.close();
		    
		    //execute the add method
			AddEntryToMap();
		}
	}
	
	/**
	 * This handler handles the confirmation button
	 * for the delete button
	 *
	 *
	 */
	public class DeleteEntry implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			//Close the prompt
			Stage stage = (Stage) confAddCanc.getScene().getWindow();
		    stage.close();
		    
		    //Execute the delete method
			DeleteEntryFromMap();
		}
	}
	
	/**
	 * This handler handles the confirmation button
	 * for modify entry.
	 * 
	 *
	 */
	public class ModifyEntry implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			//Close prompt
			Stage stage = (Stage) confAddCanc.getScene().getWindow();
		    stage.close();
		    
		    //Execute modify method
			ModifyEntryFromMap();
		}
	}
	
	/**
	 * This handler handles the add button.
	 * It executes the confirmation screen.
	 *
	 */
	public class ConfirmAdd implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			//Execute method to confirm addition
			ConfAdd(1);	
		}
	}
	
	/**
	 * This handler is for the delete button.
	 * It executes the confirmation scene.
	 *
	 */
	public class ConfirmDelete implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			//Execute method to confirm deletion
			ConfAdd(3);	
		}
	}
	
	/**
	 * This handler is for the modify button.
	 * It executes the confirmation to modify.
	 * 
	 *
	 */
	public class ConfirmModify implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			//Execute confirmation to modify
			ConfAdd(2);	
		}
	}
	
	/**
	 * This handler is for canceling addition, deletion, and
	 * modification.
	 * 
	 *
	 */
	public class CancelAdd implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			//Close the confirmation scene
			Stage stage = (Stage) confAddCanc.getScene().getWindow();
		    stage.close();
		}
	}
	
	public class Cancel implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			//Close the confirmation scene
			Stage stage = (Stage) errorBtn.getScene().getWindow();
		    stage.close();
		}
	}
	
	/**
	 * This handler is for the exit button.
	 * It executes the save functionality.
	 * 
	 *
	 */
	public class ExitApp implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			//Execute save method
			ExitSave();	
		}
	}
	
	//*****************************************************
	//File Reader
	//*****************************************************
	
	/**
	 * This method opens, reads, and saves file data to the Map.
	 * It takes a File argument and returns a string for file
	 * read status.
	 * @param file- file of user entered music DB entries
	 * @return String- status of file read
	 */
	public String MakeMap(File file)
	{
		//Create variables to store to
		String fileStatus;
		String tempRead;
		String[] tempRead2 = new String[2];
		String item;
		String[] titlData = new String[4];
		MusicMeta tempObj;
		
		//Use try/catch for file IO exceptions
		try 
		{
			//Use scanner to read file
			Scanner readFile = new Scanner(file);
			
			//Save the placeholder to the Map
			tempObj = new MusicMeta();
			tempObj.setMusic(" ", " ", " ", " ", " ");
			musicMap.put(" ", tempObj);
			
			//Read entire file
			while(readFile.hasNext())
			{
				//Read title field in entry
				tempRead = readFile.nextLine();
				tempRead2 = tempRead.split(":");
				
				if(tempRead2.length == 2)
				{
					//If title is not blank
					if(tempRead2[1] != "" && tempRead2[1] != " ")
					{
						//Store the title
						item = tempRead2[1];
						
						
						//Cycle through all fields in entry
						for(int i=0; i<4; i++)
						{
							//Store the line
							tempRead = readFile.nextLine();
							tempRead2 = tempRead.split(":");
							
							//If entry is not blank
							if(tempRead2.length == 2)
							{
								//Store entry
								titlData[i]= tempRead2[1];
							}
							
							//Store blank for blank entry
							else
							{
								titlData[i] = "";
							}
						}
						
						//Store entry to new object
						tempObj = new MusicMeta();
						tempObj.setMusic(item, titlData[0], 
								titlData[1], titlData[2], titlData[3]);
						
						//Add new object to Map
						musicMap.putIfAbsent(item, tempObj);
						
						//Skip blank line
						readFile.nextLine();
					}
					
					//If title empty, skip all entries
					else
					{
						for(int i=0; i<4; i++)
						{
							readFile.nextLine();
						}
					}
				}
				
				//If title empty, skip all entries
				else
				{
					for(int i=0; i<4; i++)
					{
						readFile.nextLine();
					}
				}
			}
			//Save success message to status string
			fileStatus = "File loaded correctly.";
		} 
		
		//If file does not exist
		catch (FileNotFoundException e) 
		{
			//Create and store placeholder
			tempObj = new MusicMeta();
			tempObj.setMusic(" ", " ", " ", " ", " ");
			musicMap.put(" ", tempObj);
			
			//Save fail message to status string
			fileStatus = "Error: Check file.";
		}
		
		//Return status string
		return fileStatus;
	}

}
