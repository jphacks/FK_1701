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
import android.os.Vibrator
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
    val Log = Logger.getLogger(connectBluethooth::class.java.name)
    var connectFlag = false
    var mmOutputStream: OutputStream? = null
    var mSocket: BluetoothSocket? = null
    var startFlag = false

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

        bluetoothEnabled()
        checkPermission()

        val img_b = findViewById(R.id.image_button) as ImageButton
//        var btAdapter = BluetoothAdapter.getDefaultAdapter();
//        var btDevices = btAdapter.getBondedDevices();
//        for (device in btDevices) {
//            if (device.getName() == SERVICE_UUID) {
//                var mmSocket = device.createRfcommSocketToServiceRecord(MY_UUID)
//                mmSocket.connect()
//                mSocket = mmSocket
//                var mOutputStream = mmSocket.getOutputStream()
//                mmOutputStream = mOutputStream
//            }
//        }

        img_b.setOnClickListener {
            connectFlag = true
            var btAdapter = BluetoothAdapter.getDefaultAdapter();
            var btDevices = btAdapter.getBondedDevices();
            for (device in btDevices) {
                if (device.getName() == SERVICE_UUID) {
                    var mmSocket = device.createRfcommSocketToServiceRecord(MY_UUID)
                    mmSocket.connect()
                    mSocket = mmSocket
                    var mOutputStream = mmSocket.getOutputStream()
                    mmOutputStream = mOutputStream
                }
            }
            setTimeSet()
        }



    }

    var studyTime:Long = 30000


    fun setTimeSet(){
        setContentView(R.layout.settime)



//        var hour:Int = 1
        var mine = 0
        var seco = 30
        var radio:RadioButton
        var timerText = findViewById(R.id.times) as TextView
//        val hourA = findViewById(R.id.hour) as Button
        val minA = findViewById(R.id.minA) as Button
        val secA = findViewById(R.id.secA) as Button
        val start = findViewById(R.id.start) as Button

        val radioGroup = findViewById(R.id.radiogroup) as RadioGroup
        // 指定した ID のラジオボタンをチェック
        radioGroup.check(R.id.radiobutton5)
        // チェックされているラジオボタンの ID を取得




        timerText!!.text = "00:30"

//        hourA.setOnClickListener {
//            hour = hour +1
//            if(hour == 24 )hour = 0
//            timerText!!.text = String.format("%2$02d:%2$02d:%2$02d", hour, min, sec)
//        }

        minA.setOnClickListener {
            mine = mine + 1
            studyTime += 60000
            if(mine == 60 ){
                mine = 0
                studyTime -= 3600000
            }
            showTime(timerText,mine,seco)
        }

        secA.setOnClickListener {
            seco = seco +1
            studyTime += 1000
            if(seco == 60){
                seco = 0
                studyTime -= 60000
            }
            showTime(timerText,mine,seco)
//            timerText!!.text = String.format("%2$02d:%2$02d", mine, seco)
        }


        start.setOnClickListener {
            val id = radioGroup.getCheckedRadioButtonId()-2131427450
            if(id == 0)mmOutputStream!!.write("1".toByteArray())
            else mmOutputStream!!.write("2".toByteArray())
//            mmOutputStream!!.write("1".toByteArray())
            mmOutputStream!!.write("5".toByteArray())
            setScreenSub()
        }



    }

    fun showTime(ttext:TextView,mm:Int,ss:Int){
        var mv = ""
        var sv = ""
        if(mm>9) mv =""+ mm
        else mv = "0" + mm

        if(ss>9) sv = "" + ss
        else sv = "0" + ss
        ttext!!.text = mv + ":" + sv

    }

    fun showTime(ttext:TextView,mm:Long,ss:Long){
        var mv = ""
        var sv = ""
        if(mm>9) mv =""+ mm
        else mv = "0" + mm

        if(ss>9) sv = "" + ss
        else sv = "0" + ss
        ttext!!.text = mv + ":" + sv

    }




    var timerText2 : TextView? = null
    fun setScreenSub(){
        setContentView(R.layout.timer)
        timerText2 = findViewById(R.id.timer) as TextView


        timerText2!!.text = "00:00"

        val countDown = CountDown(studyTime, 100)
        studyTime = 30000
        countDown.start()

    }


    internal inner class CountDown(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        var countZero = 0
        var biriFlag = false


        override fun onFinish() {
            // 完
            timerText2!!.text = "00:00"
            setschereenTimeup(countZero)
        }

        // インターバルで呼ばれる
        val buffer = ByteArray(1024)
        var bytes: Int =0
        var mmInStream: InputStream? = null


        override fun onTick(millisUntilFinished: Long) {

            // 残り時間を分、秒に分割
//            val hm = millisUntilFinished / 1000
            val mm = (millisUntilFinished /1000 / 60)
            val ss = (millisUntilFinished / 1000 % 60)
            showTime(timerText2!!,mm,ss)
//            timerText2!!.text = String.format("%2$02d:%2$02d", mm, ss)

            mmInStream = mSocket!!.inputStream
            bytes = mmInStream!!.read(buffer)
                // String型に変換
            val readMsg = String(buffer, 0, bytes)
            if(readMsg=="1" && biriFlag == false){
                countZero = countZero +1
                biriFlag = true
            }
            else biriFlag=false
        }
    }

    fun setschereenTimeup(count:Int){
        setContentView(R.layout.timeup)
        mmOutputStream!!.write("6".toByteArray())

        val timeupComment = findViewById(R.id.timeupComment) as TextView
        val fin = findViewById(R.id.fin) as TextView
        val timerSet = findViewById(R.id.button) as Button

        timeupComment!!.text="あなたは"+(count/65)+"回ビリビリにあいました。"

        if(count == 0)fin.setText("よくできました")
        else if(3 >= count/65)fin.setText("もう少しですね。次はもっと勉強頑張りましょう")
        else fin.setText("勉強しろ〜〜〜")

        timerSet.setOnClickListener {
            setTimeSet()
        }
    }



    companion object {
        private val SERVICE_UUID = "RNBT-D6E4"
        private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }


}


