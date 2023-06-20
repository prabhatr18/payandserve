package com.digital.payandserve.posprinter;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import static com.digital.payandserve.posprinter.AEMPrinter.ESCAPE_SEQ;

public class PosPrinter extends AppCompatActivity implements IAemCardScanner, IAemScrybe {

    AEMPrinter m_AemPrinter = null;
    AEMScrybeDevice m_AemScrybeDevice;
    ArrayList<String> printerList;
    ImageView btnprint, btnprint1;
    char[] batteryStatusCommand = new char[]{0x1B, 0x7E, 0x42, 0x50, 0x7C, 0x47, 0x45, 0x54, 0x7C, 0x42, 0x41, 0x54, 0x5F, 0x53, 0x54, 0x5E};
    CardReader m_cardReader = null;
    private ListView lstvw;
    private ArrayAdapter aAdapter;
    ArrayList listss = new ArrayList();
    private BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_invoice);
        printerList = new ArrayList<String>();
        m_AemScrybeDevice = new AEMScrybeDevice(this);
        m_AemPrinter = m_AemScrybeDevice.getAemPrinter();


        btnprint1 = findViewById(R.id.imgPrint);
       
        btnprint1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bAdapter == null) {
                    Toast.makeText(getApplicationContext(), "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
                } else {
                    openDialog(v);
                }

            }
        });
    }


    public void onShowPairedPrinters(String printerName) {
//        printerList = m_AemScrybeDevice.getPairedPrinters();
//        if (printerList.size() > 0) {
            try {
                m_AemScrybeDevice.connectToPrinter(printerName);
             //   m_cardReader = m_AemScrybeDevice.getCardReader(this);
                m_AemPrinter = m_AemScrybeDevice.getAemPrinter();
                Toast.makeText(PosPrinter.this, "Connected with " + printerName, Toast.LENGTH_SHORT).show();
                String data = new String(batteryStatusCommand);
                m_AemPrinter.print(data);
                if (m_AemPrinter == null) {
                    Toast.makeText(PosPrinter.this, "Printer not connected", Toast.LENGTH_SHORT).show();
                } else {
                    print();
                }
            } catch (IOException e) {
                if (e.getMessage().contains("Service discovery failed")) {
                    Toast.makeText(PosPrinter.this, "Not Connected\n" + printerName + " is unreachable or off otherwise it is connected with other device", Toast.LENGTH_SHORT).show();
                } else if (e.getMessage().contains("Device or resource busy")) {
                    Toast.makeText(PosPrinter.this, "the device is already connected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PosPrinter.this, "Unable to connect", Toast.LENGTH_SHORT).show();
                }
            }
//        } else
//            showAlert("No Paired Printers found");
    }

    public void print() {

        try {
            m_AemPrinter.setFontType(ESCAPE_SEQ);//esc
            String data = "Don't Fear \n When Ravi is here";
            String data1 = "Don't Fear \n we are here ";
            m_AemPrinter.print(data);
            m_AemPrinter.print(data1);
            m_AemPrinter.setCarriageReturn();
            m_AemPrinter.setCarriageReturn();
            m_AemPrinter.setCarriageReturn();
            m_AemPrinter.setCarriageReturn();


//            m_AemPrinter.setFontType(ESCAPE_SEQ);//esc
//            m_AemPrinter.setFontType(ESCAPE_a);//a
//            m_AemPrinter.setFontType(ESCAPE_CENTER);//0x01
//            m_AemPrinter.setFontType(ESCAPE_SEQ);//esc
//            m_AemPrinter.setFontType(ESCAPE_EXCL);//!
//            m_AemPrinter.setFontType(ESCAPE_DOUBLE_HEIGHT);//esc
//            data = "THREE INCH PRINTER: TEST PRINT \n";
//            m_AemPrinter.print(data);
//            m_AemPrinter.setCarriageReturn();
//            data = 	"CODE|   DESCRIPTION   |RATE(Rs)|QTY |AMOUNT(Rs)\n";
//            m_AemPrinter.POS_FontThreeInchTAHOMA();
//            m_AemPrinter.print(data);
//            m_AemPrinter.setCarriageReturn();
//
//            data = " 13 |Colgate Total Gel | 35.00  | 02 |  70.00\n"+
//                    " 29 |Pears Soap 250g   | 25.00  | 01 |  25.00\n"+
//                    " 88 |Lux Shower Gel 500| 46.00  | 01 |  46.00\n"+
//                    " 15 |Dabur Honey 250g  | 65.00  | 01 |  65.00\n"+
//                    " 52 |Cadbury Dairy Milk| 20.00  | 10 | 200.00\n"+
//                    " 29 |Pears Soap 250g   | 25.00  | 01 |  25.00\n"+
//                    " 88 |Lux Shower Gel 500| 46.00  | 01 |  46.00\n"+
//                    " 15 |Dabur Honey 250g  | 65.00  | 01 |  65.00\n"+
//                    " 52 |Cadbury Dairy Milk| 20.00  | 10 | 200.00\n"+
//                    "128 |Maggie Totamto Sou| 36.00  | 04 | 144.00\n";
//
//            m_AemPrinter.POS_FontThreeInchTAHOMA();
//            m_AemPrinter.print(data);
//            data = "TOTAL AMOUNT (Rs.)   550.00\n";
//            m_AemPrinter.POS_FontThreeInchCENTER();
//            m_AemPrinter.print(data);
//            data = "Thank you! \n";
//            m_AemPrinter.POS_FontThreeInchCENTER();
//            m_AemPrinter.POS__FontThreeInchDOUBLEHIEGHT();
//            m_AemPrinter.print(data);
//            m_AemPrinter.setCarriageReturn();
//            m_AemPrinter.setCarriageReturn();
//            m_AemPrinter.setCarriageReturn();
//            m_AemPrinter.setCarriageReturn();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onScanMSR(String buffer, CardReader.CARD_TRACK cardtrack) {

    }

    @Override
    public void onScanDLCard(String buffer) {

    }

    @Override
    public void onScanRCCard(String buffer) {

    }

    @Override
    public void onScanRFD(String buffer) {

    }

    @Override
    public void onScanPacket(String buffer) {

    }

    @Override
    public void onDiscoveryComplete(ArrayList<String> aemPrinterList) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select Printer to connect");
        Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < printerList.size(); i++) {
            menu.add(0, v.getId(), 0, printerList.get(i));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        String printerName = item.getTitle().toString();
        try {
            m_AemScrybeDevice.connectToPrinter(printerName);
            m_cardReader = m_AemScrybeDevice.getCardReader(this);
            m_AemPrinter = m_AemScrybeDevice.getAemPrinter();
            Toast.makeText(PosPrinter.this, "Connected with " + printerName, Toast.LENGTH_SHORT).show();
            String data = new String(batteryStatusCommand);
            m_AemPrinter.print(data);
            //  m_cardReader.readMSR();
        } catch (IOException e) {
            if (e.getMessage().contains("Service discovery failed")) {
                Toast.makeText(PosPrinter.this, "Not Connected\n" + printerName + " is unreachable or off otherwise it is connected with other device", Toast.LENGTH_SHORT).show();
            } else if (e.getMessage().contains("Device or resource busy")) {
                Toast.makeText(PosPrinter.this, "the device is already connected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PosPrinter.this, "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    public void list() {
        Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
        for (BluetoothDevice bt : pairedDevices) listss.add(bt.getName());
        Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
        ListView lstvw = (ListView) findViewById(R.id.listView);
        final ArrayAdapter adapter = new ArrayAdapter(this, R.layout.cutom_alert_popup_blutooth, listss);

        lstvw.setAdapter(adapter);

        lstvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String selected = (String) listss.get(position);
                for (Iterator<BluetoothDevice> it = pairedDevices.iterator(); it.hasNext(); ) {
                    BluetoothDevice bt = it.next();
                    if (bt.getName().equals(selected)) {
                        Toast.makeText(PosPrinter.this, "" + bt.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void openDialog(View v) {
        AlertDialog.Builder alertDialog = new
                AlertDialog.Builder(this);
        View rowList = getLayoutInflater().inflate(R.layout.cutom_alert_popup_blutooth, null);
        Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
        for (BluetoothDevice bt : pairedDevices) listss.add(bt.getName());
        Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
        lstvw = rowList.findViewById(R.id.listView);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listss);
        lstvw.setAdapter(adapter);
        lstvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String selected = (String) listss.get(position);
                for (Iterator<BluetoothDevice> it = pairedDevices.iterator(); it.hasNext(); ) {
                    BluetoothDevice bt = it.next();
                    if (bt.getName().equals(selected)) {
                        onShowPairedPrinters(""+bt.getName());
                    }
                    //connect to device

                }
            }
        });
        adapter.notifyDataSetChanged();
        alertDialog.setView(rowList);
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }
}