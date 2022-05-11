import com.example.newgonggong.MapsViewModel
import com.example.newgonggong.Presentation.di.buildOkHttpClient
import com.example.newgonggong.Presentation.di.provideAPIService
import com.example.newgonggong.Presentation.di.provideGsonConverterFactory
import com.example.newgonggong.Presentation.di.provideRetrofit
import com.example.newgonggong.data.repository.CardRepositoryImpl
import com.example.newgonggong.data.repository.dataSource.CardRemoteDataSource
import com.example.newgonggong.data.repository.dataSourceImpl.CardRemoteDataSourceImpl
import com.example.newgonggong.domain.CardRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<CardRepository> { CardRepositoryImpl(get()) }
    single<CardRemoteDataSource> { CardRemoteDataSourceImpl(get())}

    viewModel{ MapsViewModel(androidApplication(), get())}

    single { provideGsonConverterFactory()}
    single { buildOkHttpClient()}
    single { provideRetrofit(get(), get())}
    single{ provideAPIService(get())}
}