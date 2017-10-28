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
import android.bluetooth.BluetoothSocket
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import java.io.InputStream
import java.io.OutputStream
import java.util.*


import java.util.logging.Logger


class connectBluethooth : AppCompatActivity() {

    internal var REQUEST_ENABLE_BT = 1
    var data = mutableListOf("aaa","aaaaaa")
    val Log = Logger.getLogger(connectBluethooth::class.java.name)
    var connectFlag = false
    var mmOutputStream: OutputStream? = null
    var mSocket: BluetoothSocket? = null
    var startFlag = false
    var count = 0

    private var mInputTextView: TextView? = null

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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenMain()
    }

    fun setScreenMain(){
        setContentView(R.layout.activity_connect_bluethooth)
        val textView = findViewById(R.id.texts) as TextView
        val connect = findViewById(R.id.button) as Button
        val start = findViewById(R.id.button2) as Button
        val fin = findViewById(R.id.button3) as Button


        bluetoothEnabled()
        checkPermission()

        connect.setOnClickListener {
            connectFlag = true
            var btAdapter = BluetoothAdapter.getDefaultAdapter();
            var btDevices = btAdapter.getBondedDevices();
            var devList = ""
            for (device in btDevices) {
                if (device.getName() == SERVICE_UUID) {
                    var mmSocket = device.createRfcommSocketToServiceRecord(MY_UUID)
                    mmSocket.connect()
                    mSocket = mmSocket
                    var mOutputStream = mmSocket.getOutputStream()
                    mmOutputStream = mOutputStream
                }
            }
            textView.setText(devList)
        }

        start.setOnClickListener {
            //            if(connectFlag){
//                startFlag = true
//                val buffer = ByteArray(1024)
//                var bytes: Int
//                var mmInStream: InputStream? = null
//                while(startFlag){
//                    mmInStream = mSocket!!.inputStream
//                    bytes = mmInStream!!.read(buffer)
//                // String型に変換
//                    val readMsg = String(buffer, 0, bytes)
//                    if(readMsg=="1"){
//                        count = count+1
//                    }
////                    textView.setText(count)
//                }
            setScreenSub()
//                mmOutputStream!!.write("1".toByteArray())
//            }
        }

        fin.setOnClickListener {
            if(connectFlag){
                startFlag = false
//                mmOutputStream!!.write("0".toByteArray())

            }
        }

    }

    var timerText2 : TextView? = null
    fun setScreenSub(){
        setContentView(R.layout.timer)
        timerText2 = findViewById(R.id.timer) as TextView


        timerText2!!.text = "0:00.000"

        val countDown = CountDown(180000, 100)
        countDown.start()

    }
    internal inner class CountDown(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            // 完了
            timerText2!!.text = "0:00.000"
        }

        // インターバルで呼ばれる
        val buffer = ByteArray(1024)
        var bytes: Int
        var mmInStream: InputStream? = null
        override fun onTick(millisUntilFinished: Long) {
            // 残り時間を分、秒、ミリ秒に分割
            val mm = millisUntilFinished / 1000 / 60
            val ss = millisUntilFinished / 1000 % 60
            val ms = millisUntilFinished - ss * 1000 - mm * 1000 * 60

            timerText2!!.text = String.format("%1$02d:%2$02d.%3$03d", mm, ss, ms)

//                mmInStream = mSocket!!.inputStream
//                bytes = mmInStream!!.read(buffer)
//                // String型に変換
//                val readMsg = String(buffer, 0, bytes)
//                if(readMsg=="1"){

//                    count = count+1
//                }
        }
    }

    fun setschereenTimeup(){
        
    }



    companion object {
        private val SERVICE_UUID = "RNBT-D6E4"
        private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }


}


