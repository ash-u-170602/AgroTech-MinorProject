package com.example.KrishiCare.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController

import com.example.KrishiCare.api.RetrofitInstance
import com.example.KrishiCare.model.CurrentWeather
import com.example.krishicare.R
import com.example.krishicare.databinding.SecondFragmentBinding
import com.example.krishicare.ml.ConvertedModel
import com.example.krishicare.ui.fragment.BaseFragments
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class SecondFragment: BaseFragments() {
    companion object{
        lateinit var cropName : String
    }
    lateinit var state: String
    private val binding by lazy { SecondFragmentBinding.inflate(layoutInflater)}
    private val outputIndexes = arrayListOf(
        "apple",
        "banana",
        "blackgram",
        "chickpea",
        "coconut",
        "coffee",
        "cotton",
        "grapes",
        "jute",
        "kidneybeans",
        "lentil",
        "maize",
        "mango",
        "mothbeans",
        "mungbean",
        "muskmelon",
        "orange",
        "papaya",
        "pigeonpeas",
        "pomegranate",
        "rice",
        "watermelon "
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationVisibility(false)
        prepareStatesSpinner()
        binding.predict.setOnClickListener {
            getStateWeather(state)
        }
    }

    private fun prepareStatesSpinner() {
        val indianStates = arrayListOf<String>("Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh","Delhi" , "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal")
        val adapter =
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                indianStates
            )

        binding.spinnerIndianState.adapter = adapter

        binding.spinnerIndianState.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    state = indianStates[position]

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
}

    private fun createByteBufferAndPredict(data: CurrentWeather) {

        if (binding.btnnitrogen.text.toString().trim().isEmpty() ||
            binding.btnph.text.toString().trim().isEmpty() ||
            binding.btnphosp.text.toString().trim().isEmpty() ||
            binding.btnrainfall.text.toString().trim().isEmpty() ||
            binding.btnpottasium.text.toString().trim().isEmpty()
        ) {
            showToast("Please enter all values")
            return
        }

        val nitrogen = binding.btnnitrogen.text.toString().toFloat()
        val phosphorus = binding.btnphosp.text.toString().toFloat()
        val potassium = binding.btnpottasium.text.toString().toFloat()
        val temperature = data.main.temp.toFloat()
        val humidity = data.main.humidity.toFloat()
        val ph = binding.btnph.text.toString().toFloat()
        val rainfall = binding.btnrainfall.text.toString().toFloat()

        val inputData =
            floatArrayOf(nitrogen, phosphorus, potassium, temperature, humidity, ph, rainfall)

        val numBytes = inputData.size * java.lang.Float.SIZE / java.lang.Byte.SIZE

        val byteBuffer = ByteBuffer.allocateDirect(numBytes).order(ByteOrder.nativeOrder())

        for (value in inputData) {
            byteBuffer.putFloat(value)
        }

        byteBuffer.rewind()

        val model = ConvertedModel.newInstance(requireContext())

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 7), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        model.close()

        val output = outputFeature0.floatArray

        val finalPrediction = output.indexOfMax()
        showToast(finalPrediction.toString())

        if (finalPrediction == null) {
            showToast("Something Went Wrong!!")
        } else {
            val crop = try {
                outputIndexes[finalPrediction]
            } catch (e: Exception) {
                "Index Out of Bounds"
            }
            cropName = crop
            showToast(crop)

          findNavController().navigate(R.id.action_secondFragment_to_cropResult3)

        }
    }

    private fun getStateWeather(state: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = try {
                RetrofitInstance.api.getCurrentWeather(
                    state,
                    "in",
                    "metric",
                    "d1d76e6f74c25f60b602ac34a75cc0e5"
                )

            } catch (e: IOException) {
                showToast("app error ${e.message}")
                return@launch
            } catch (e: IOException) {
                showToast("http error ${e.message}")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {

                    val data = response.body()!!

                    createByteBufferAndPredict(data)
                }
            }

        }
    }
    private fun FloatArray.indexOfMax(): Int? {
        if (isEmpty()) {
            return null
        }

        var maxIndex = 0
        var maxValue = this[0]

        for (i in 1 until size) {
            if (this[i] > maxValue) {
                maxIndex = i
                maxValue = this[i]
            }
        }

        return maxIndex
    }
    }