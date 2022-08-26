package cvb.com.br.teste

import PrefUtil
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import cvb.com.br.teste.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val prefUtil by lazy { PrefUtil(applicationContext) }

    private lateinit var mBinding: ActivityMainBinding

    private val listFlag = arrayListOf<ImageButton>()

    private val listSticker = arrayListOf<AppCompatButton>()

    private lateinit var prefix: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        loadListFlag()

        loadListSticker()

        setListeners()

        disableFlags()
    }

    private fun loadListFlag() {
        listFlag.add(mBinding.btQAT)
        listFlag.add(mBinding.btECU)
        listFlag.add(mBinding.btSEN)
        listFlag.add(mBinding.btNED)
        listFlag.add(mBinding.btENG)
        listFlag.add(mBinding.btIRN)

        listFlag.add(mBinding.btUSA)
        listFlag.add(mBinding.btWAL)
        listFlag.add(mBinding.btARG)
        listFlag.add(mBinding.btKSA)
        listFlag.add(mBinding.btMEX)
        listFlag.add(mBinding.btPOL)

        listFlag.add(mBinding.btFRA)
        listFlag.add(mBinding.btAUS)
        listFlag.add(mBinding.btDEN)
        listFlag.add(mBinding.btTUN)
        listFlag.add(mBinding.btESP)
        listFlag.add(mBinding.btCRC)

        listFlag.add(mBinding.btGER)
        listFlag.add(mBinding.btJPN)
        listFlag.add(mBinding.btBEL)
        listFlag.add(mBinding.btCAN)
        listFlag.add(mBinding.btMAR)
        listFlag.add(mBinding.btCRO)

        listFlag.add(mBinding.btBRA)
        listFlag.add(mBinding.btSRB)
        listFlag.add(mBinding.btSUI)
        listFlag.add(mBinding.btCMR)
        listFlag.add(mBinding.btPOR)
        listFlag.add(mBinding.btGHA)

        listFlag.add(mBinding.btURU)
        listFlag.add(mBinding.btKOR)
        listFlag.add(mBinding.btFWC)
        listFlag.add(mBinding.btCOC)
    }

    private fun loadListSticker() {
        listSticker.add(mBinding.fig001)
        listSticker.add(mBinding.fig002)
        listSticker.add(mBinding.fig003)
        listSticker.add(mBinding.fig004)
        listSticker.add(mBinding.fig005)
        listSticker.add(mBinding.fig006)
        listSticker.add(mBinding.fig007)
        listSticker.add(mBinding.fig008)
        listSticker.add(mBinding.fig009)
        listSticker.add(mBinding.fig010)

        listSticker.add(mBinding.fig011)
        listSticker.add(mBinding.fig012)
        listSticker.add(mBinding.fig013)
        listSticker.add(mBinding.fig014)
        listSticker.add(mBinding.fig015)
        listSticker.add(mBinding.fig016)
        listSticker.add(mBinding.fig017)
        listSticker.add(mBinding.fig018)
        listSticker.add(mBinding.fig019)
        listSticker.add(mBinding.fig020)

        listSticker.add(mBinding.fig021)
        listSticker.add(mBinding.fig022)
        listSticker.add(mBinding.fig023)
        listSticker.add(mBinding.fig024)
        listSticker.add(mBinding.fig025)
        listSticker.add(mBinding.fig026)
        listSticker.add(mBinding.fig027)
        listSticker.add(mBinding.fig028)
        listSticker.add(mBinding.fig029)
        listSticker.add(mBinding.fig030)
    }

    private fun setListeners() {
        listFlag.forEach { imageButton ->
            imageButton.setOnClickListener { loadStickersByFlag(imageButton) }
        }

        listSticker.forEach { appCompatButton ->
            appCompatButton.setOnClickListener { configStickerStatus(appCompatButton) }
        }
    }

    private fun configStickerStatus(appCompatButton: AppCompatButton) {
        val number = appCompatButton.text

        val currentStatus: Boolean = !(appCompatButton.tag as Boolean)

        prefUtil.setPrefBoolean("${prefix}_${number}", currentStatus)

        appCompatButton.tag = currentStatus
        appCompatButton.background = if (currentStatus)
            resources.getDrawable(R.drawable.bt_shape_circular_ok)
        else
            resources.getDrawable(R.drawable.bt_shape_circular)

        updateInfoQtd()
    }

    private fun disableFlags() {
        listFlag.forEach { imageButton ->
            imageButton.alpha = 0.5F
        }
    }

    private fun loadStickersByFlag(imageButton: ImageButton) {
        this.prefix = (imageButton.tag) as String

        disableFlags()

        imageButton.alpha = 1.0F

        val maxIdx = getMaxSticker()

        listSticker.forEach { imageButton ->
            imageButton.background = resources.getDrawable(R.drawable.bt_shape_circular)
            imageButton.visibility = View.INVISIBLE
            imageButton.tag = false
        }

        for (idx in 0 until maxIdx) {

            val n = (if (prefix == Constant.C_FWC) idx else idx + 1)
            val number = n.toString().padStart(2, '0')

            listSticker[idx].visibility = View.VISIBLE
            listSticker[idx].text = number

            val status = prefUtil.getPrefBoolean("${prefix}_${number}")

            if (status) {
                listSticker[idx].background = resources.getDrawable(R.drawable.bt_shape_circular_ok)
                listSticker[idx].tag = true
            }
        }

        updateInfoFlag()

        updateInfoQtd()

        mBinding.constraint.visibility = View.VISIBLE
    }

    private fun getMaxSticker(): Int {
        return when (prefix) {
            Constant.C_FWC -> { Constant.MAX_NUMBER_FWC }
            Constant.C_COC -> { Constant.MAX_NUMBER_COCA }
            else -> { Constant.MAX_NUMBER_GERAL }
        }
    }

    private fun getStatusByFlag():Int {
        var qtd = 0

        val maxIdx = getMaxSticker()

        for (idx in 0 until maxIdx) {

            val n = (if (prefix == Constant.C_FWC) idx else idx + 1)
            val number = n.toString().padStart(2, '0')

            val status = prefUtil.getPrefBoolean("${prefix}_${number}")

            if (!status) {
                qtd++
            }
        }

        return qtd
    }

    private fun updateInfoFlag() {
        val id =
        when (this.prefix) {
                Constant.C_QAT -> { R.string.QAT_desc }
                Constant.C_ECU -> { R.string.ECU_desc }
                Constant.C_SEN -> { R.string.SEN_desc }
                Constant.C_NED -> { R.string.NED_desc }

                Constant.C_ENG -> { R.string.ENG_desc }
                Constant.C_IRN -> { R.string.IRN_desc }
                Constant.C_USA -> { R.string.USA_desc }
                Constant.C_WAL -> { R.string.WAL_desc }

                Constant.C_ARG -> { R.string.ARG_desc }
                Constant.C_KSA -> { R.string.KSA_desc }
                Constant.C_MEX -> { R.string.MEX_desc }
                Constant.C_POL -> { R.string.POL_desc }

                Constant.C_FRA -> { R.string.FRA_desc }
                Constant.C_AUS -> { R.string.AUS_desc }
                Constant.C_DEN -> { R.string.DEN_desc }
                Constant.C_TUN -> { R.string.TUN_desc }

                Constant.C_ESP -> { R.string.ESP_desc }
                Constant.C_CRC -> { R.string.CRC_desc }
                Constant.C_GER -> { R.string.GER_desc }
                Constant.C_JPN -> { R.string.JPN_desc }

                Constant.C_BEL -> { R.string.BEL_desc }
                Constant.C_CAN -> { R.string.CAN_desc }
                Constant.C_MAR -> { R.string.MAR_desc }
                Constant.C_CRO -> { R.string.CRO_desc }

                Constant.C_BRA -> { R.string.BRA_desc }
                Constant.C_SRB -> { R.string.SRB_desc }
                Constant.C_SUI -> { R.string.SUI_desc }
                Constant.C_CMR -> { R.string.CMR_desc }

                Constant.C_POR -> { R.string.POR_desc }
                Constant.C_GHA -> { R.string.GHA_desc }
                Constant.C_URU -> { R.string.URU_desc }
                Constant.C_KOR -> { R.string.KOR_desc }

                Constant.C_FWC -> { R.string.FWC_desc }
                Constant.C_COC -> { R.string.COC_desc }
                else -> -1
            }

        mBinding.tvFlag.text = "${getString(id)}   [ $prefix ] "
    }

    private fun updateInfoQtd() {
        mBinding.tvQtd.text = getString(R.string.falta_sticker, getStatusByFlag().toString())
    }
}
