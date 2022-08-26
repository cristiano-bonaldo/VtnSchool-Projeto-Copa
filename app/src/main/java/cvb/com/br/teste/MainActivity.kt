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

        val maxIdx = getMaxSticker(prefix)

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

    private fun getMaxSticker(flag: String): Int {
        return when (flag) {
            Constant.C_FWC -> { Constant.MAX_NUMBER_FWC }
            Constant.C_COC -> { Constant.MAX_NUMBER_COCA }
            else -> { Constant.MAX_NUMBER_GERAL }
        }
    }

    private fun getStatusByFlag(flag: String):Int {
        var qtd = 0

        val maxIdx = getMaxSticker(flag)

        for (idx in 0 until maxIdx) {

            val n = (if (flag == Constant.C_FWC) idx else idx + 1)
            val number = n.toString().padStart(2, '0')

            val status = prefUtil.getPrefBoolean("${flag}_${number}")

            if (!status) {
                qtd++
            }
        }

        return qtd
    }

    private fun getStatusAllFlag():Int {
        var qtd = 0

        listFlag.forEach { imageButton ->
            var q = getStatusByFlag(imageButton.tag as String)

            Log.i("CVB", (imageButton.tag as String) + "-> " + q)
            qtd += q
        }

        return qtd
    }

    private fun updateInfoFlag() {
        val id: Int
        val im: Int

        when (this.prefix) {
            Constant.C_QAT -> {
                id = R.string.QAT_desc
                im = R.drawable.band_quatar
            }

            Constant.C_ECU -> {
                id = R.string.ECU_desc
                im = R.drawable.band_equador
            }

            Constant.C_SEN -> {
                id = R.string.SEN_desc
                im = R.drawable.band_senegal
            }

            Constant.C_NED -> {
                id = R.string.NED_desc
                im = R.drawable.band_holanda
            }

            Constant.C_ENG -> {
                id = R.string.ENG_desc
                im = R.drawable.band_englaterra
            }

            Constant.C_IRN -> {
                id = R.string.IRN_desc
                im = R.drawable.band_iran
            }

            Constant.C_USA -> {
                id = R.string.USA_desc
                im = R.drawable.band_usa
            }

            Constant.C_WAL -> {
                id = R.string.WAL_desc
                im = R.drawable.band_gales
            }

            Constant.C_ARG -> {
                id = R.string.ARG_desc
                im = R.drawable.band_argentina
            }

            Constant.C_KSA -> {
                id = R.string.KSA_desc
                im = R.drawable.band_arabia
            }

            Constant.C_MEX -> {
                id = R.string.MEX_desc
                im = R.drawable.band_mexico
            }

            Constant.C_POL -> {
                id = R.string.POL_desc
                im = R.drawable.band_polonia
            }


            Constant.C_FRA -> {
                id = R.string.FRA_desc
                im = R.drawable.band_franca
            }

            Constant.C_AUS -> {
                id = R.string.AUS_desc
                im = R.drawable.band_australia
            }

            Constant.C_DEN -> {
                id = R.string.DEN_desc
                im = R.drawable.band_dinamarca
            }

            Constant.C_TUN -> {
                id = R.string.TUN_desc
                im = R.drawable.band_tunisia
            }

            Constant.C_ESP -> {
                id = R.string.ESP_desc
                im = R.drawable.band_espanha
            }

            Constant.C_CRC -> {
                id = R.string.CRC_desc
                im = R.drawable.band_costa_rica
            }

            Constant.C_GER -> {
                id = R.string.GER_desc
                im = R.drawable.band_alemanha
            }

            Constant.C_JPN -> {
                id = R.string.JPN_desc
                im = R.drawable.band_japao
            }


            Constant.C_BEL -> {
                id = R.string.BEL_desc
                im = R.drawable.band_belgica
            }

            Constant.C_CAN -> {
                id = R.string.CAN_desc
                im = R.drawable.band_canada
            }

            Constant.C_MAR -> {
                id = R.string.MAR_desc
                im = R.drawable.band_marrocos
            }

            Constant.C_CRO -> {
                id = R.string.CRO_desc
                im = R.drawable.band_croacia
            }

            Constant.C_BRA -> {
                id = R.string.BRA_desc
                im = R.drawable.band_brasil
            }

            Constant.C_SRB -> {
                id = R.string.SRB_desc
                im = R.drawable.band_servia
            }

            Constant.C_SUI -> {
                id = R.string.SUI_desc
                im = R.drawable.band_suica
            }

            Constant.C_CMR -> {
                id = R.string.CMR_desc
                im = R.drawable.band_camaroes
            }

            Constant.C_POR -> {
                id = R.string.POR_desc
                im = R.drawable.band_portugal
            }

            Constant.C_GHA -> {
                id = R.string.GHA_desc
                im = R.drawable.band_gana
            }

            Constant.C_URU -> {
                id = R.string.URU_desc
                im = R.drawable.band_uruguai
            }

            Constant.C_KOR -> {
                id = R.string.KOR_desc
                im = R.drawable.band_korea
            }

            Constant.C_FWC -> {
                id = R.string.FWC_desc
                im = R.drawable.flag_copa_logo
            }

            Constant.C_COC -> {
                id = R.string.COC_desc
                im = R.drawable.flag_coca
            }
            else -> {
                id = -1
                im = -1
            }
        }

        mBinding.tvFlag.text = "${getString(id)}   [ $prefix ] "

        val drawable = resources.getDrawable(im)
        mBinding.ivFlag.setImageDrawable(drawable)
    }

    private fun updateInfoQtd() {
        this.title =
            getString(R.string.app_name) + " [ " +
            getString(R.string.falta_sticker, getStatusAllFlag().toString()) + " ] "

        mBinding.tvQtd.text = getString(R.string.falta_sticker, getStatusByFlag(this.prefix).toString())
    }
}
