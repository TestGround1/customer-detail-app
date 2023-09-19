package lk.ijse.dep11.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.tm.Customer;

import java.io.*;

public class MainViewConstroller {
    public AnchorPane root;
    public TextField txtCustomerID;
    public TextField txtCustomerName;
    public TextField txtCustomerAddress;
    public TableView<Customer> tblCustomer;
    public Button btnSave;
    public Button btnDelete;
    public Button btnNewCustomer;

    public void initialize() throws IOException, ClassNotFoundException {
//        btnDelete.setDisable(true);
//        txtCustomerID.requestFocus();

        tblCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomer.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        tblCustomer.getSelectionModel().selectedItemProperty().addListener(e->{
            btnDelete.setDisable(!(tblCustomer.getSelectionModel().getSelectedItem() != null));
        });

        ObservableList<Customer> customerList = tblCustomer.getItems();

        File file = new File("customer.dep");

        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);

        try{
            while (true) {
                Customer customer = (Customer) ois.readObject();
                customerList.add(customer);
            }
        }catch (Exception e){

        }finally {
            ois.close();
        }


    }
    public void btnNewCustomerOnAction(ActionEvent actionEvent) {
        txtCustomerID.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws IOException {
        if(txtCustomerID.getText().isBlank() || !isValid(txtCustomerID.getText().strip())){
            txtCustomerID.requestFocus();
            txtCustomerID.selectAll();
            return;
        } else if (txtCustomerName.getText().isBlank()) {
            txtCustomerName.requestFocus();
            txtCustomerName.selectAll();
            return;
        }else if(txtCustomerAddress.getText().isBlank()){
            txtCustomerAddress.requestFocus();
            txtCustomerAddress.selectAll();
            return;
        }

        int id = Integer.parseInt(txtCustomerID.getText());
        String name = txtCustomerName.getText().strip();
        String address = txtCustomerAddress.getText().strip();
        Customer customer = new Customer(id, name, address);
        ObservableList<Customer> customerList = tblCustomer.getItems();

        for (Customer customer1 : customerList) {
            if(customer1.getId() == id){
                txtCustomerID.requestFocus();
                txtCustomerID.selectAll();
                return;
            }
        }

        customerList.add(customer);
        File file = new File("customer.dep");
        if(!file.exists()) file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        try{
            for (Customer customer1 : customerList) {
                oos.writeObject(customer1);
            }
        }finally {
            oos.close();
        }
        btnNewCustomer.fire();
        txtCustomerID.requestFocus();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        ObservableList<Customer> customerList = tblCustomer.getItems();
        customerList.remove(tblCustomer.getSelectionModel().getSelectedItem());
    }

    boolean isValid(String input) {
        for (int i = 0; i < input.length(); i++) {
            if(!Character.isDigit(input.charAt(i))) return false;
        }
        return true;
    }
}
