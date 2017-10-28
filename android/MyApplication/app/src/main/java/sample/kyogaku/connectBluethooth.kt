package sample.kyogaku

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import java.util.*


import java.util.logging.Logger


class connectBluethooth : AppCompatActivity() {

    internal var REQUEST_ENABLE_BT = 1
    var data = mutableListOf("aaa","aaaaaa")
    val Log = Logger.getLogger(connectBluethooth::class.java.name)
    var a= "kawa"


    private fun bluetoothEnabled() {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }
        if (!mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }


    fun checkPermission() {
        // 既に許可している
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestLocationPermission()
        }// 拒否していた場合
    }

    // 許可を求める
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this@connectBluethooth,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ENABLE_BT)

        } else {
            val toast = Toast.makeText(this, "許可されないとアプリが実行できません", Toast.LENGTH_SHORT)
            toast.show()

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ENABLE_BT)

        }
    }

    internal fun getBondState(state: Int): String {
        val strState: String
        when (state) {
            BluetoothDevice.BOND_BONDED -> strState = "接続履歴あり"
            BluetoothDevice.BOND_BONDING -> strState = "接続中"
            BluetoothDevice.BOND_NONE -> strState = "接続履歴なし"
            else -> strState = "エラー"
        }
        return strState
    }

    private fun permitScanBluethooth() {
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        startActivity(discoverableIntent)
    }

    private fun connect() {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_bluethooth)
        val listView = findViewById(R.id.texts) as TextView

        bluetoothEnabled()
        checkPermission()

        var btAdapter = BluetoothAdapter.getDefaultAdapter();
        var btDevices = btAdapter.getBondedDevices();
        var devList = ""
            for (device in btDevices) {
                if(device.getName() == SERVICE_UUID){
                    var mmsocket = device.createRfcommSocketToServiceRecord(MY_UUID)
                    mmsocket.connect()
                }
            }
            listView.setText(devList)
        }


    companion object {
        private val SERVICE_UUID = "RNBT-D6E4"
        private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }
//
//    override fun onClick(v: View?){
//        super.
//    }

}

