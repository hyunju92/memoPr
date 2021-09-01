package hyunju.com.memo2020.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.edit.vm.EditViewModel
import hyunju.com.memo2020.model.ImgUriRepository
import hyunju.com.memo2020.model.MemoRepository
import hyunju.com.memo2020.model.PrefRepository

@InstallIn(ActivityComponent::class)
@Module
object Module {

    /**
     * model
     */
    @Provides
    @ActivityScoped
    fun providePrefRepository(@ApplicationContext context: Context): PrefRepository {
        return PrefRepository(context)
    }

    @Provides
    @ActivityScoped
    fun providesImgUriRepository(@ApplicationContext context: Context): ImgUriRepository {
        return ImgUriRepository(context)
    }

    @Provides
    @ActivityScoped
    fun provideMemoDatabase(@ApplicationContext context: Context): MemoDatabase {
        return MemoDatabase.get(context)
    }

    @Provides
    @ActivityScoped
    fun provideMemoRepository(memoDatabase: MemoDatabase): MemoRepository {
        return MemoRepository(memoDatabase)
    }


    /**
     * viewModel
     */
    @Provides
    @ActivityScoped
    fun provideEditViewModel(
        memoRepository: MemoRepository,
        imgUriRepository: ImgUriRepository,
        prefRepository: PrefRepository
    ): EditViewModel {
        return EditViewModel(memoRepository, imgUriRepository, prefRepository)
    }

}