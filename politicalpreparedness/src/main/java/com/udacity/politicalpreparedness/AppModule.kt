package com.udacity.politicalpreparedness

import androidx.room.Room
import com.udacity.politicalpreparedness.data.repository.*
import com.udacity.politicalpreparedness.data.source.local.CivicsDatabase
import com.udacity.politicalpreparedness.data.source.remote.*
import com.udacity.politicalpreparedness.domain.elections.ElectionsViewModel
import com.udacity.politicalpreparedness.domain.representative.RepresentativesViewModel
import com.udacity.politicalpreparedness.domain.voterinfo.VoterInfoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //Database
    single {
        Room.databaseBuilder(
            androidContext(), CivicsDatabase::class.java, "politicalpreparedness-database"
        ).build()
    }
    //Dao
    single {
        get<CivicsDatabase>().electionsDao
    }

    //Retrofit api client
    single {
        getRetrofitClient().create(CivicsRetrofitApi::class.java)
    }
    //Retrofit network data source
    single<CivicsNetworkDataSource> {
        CivicsRetrofitNetworkDataSource(get())
    }

    //Repositories
    single<ElectionRepository> {
        DefaultElectionRepository(get(), get())
    }
    single<VoterInfoRepository> {
        DefaultVoterInfoRepository(get())
    }
    single<RepresentativeRepository> {
        DefaultRepresentativeRepository(get())
    }

    //ViewModels
    viewModel {
        ElectionsViewModel(get())
    }
    viewModel {
        VoterInfoViewModel(get(), get())
    }
    viewModel {
        RepresentativesViewModel(get(),get())
    }
}
