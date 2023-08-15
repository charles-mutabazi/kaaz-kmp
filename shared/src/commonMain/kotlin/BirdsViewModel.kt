import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.BirdImage

/**
 * Project Name: MyApplication
 * Created by: MUTABAZI Charles on 15/08/2023
 */

data class BirdsUIState(
    val isLoading: Boolean = false,
    val birds: List<BirdImage> = emptyList(),
    val error: String? = null,
)

class BirdsViewModel : ViewModel() {
    private var _uiState = MutableStateFlow(BirdsUIState())
    val uiState = _uiState.asStateFlow()

    init {
        loadBirdImage()
    }

    override fun onCleared() {
        httpClient.close() // close client on viewModel cleared
    }

    fun loadBirdImage() {
        viewModelScope.launch {
            val images = getBirdImages()
            _uiState.update { it.copy(birds = images) }
        }
    }

    private suspend fun getBirdImages(): List<BirdImage> {
        return httpClient.get("https://sebi.io/demo-image-api/pictures.json").body()
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }
}
