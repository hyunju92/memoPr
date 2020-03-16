package hyunju.com.memo2020.viewmodel

import android.app.Activity
import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import hyunju.com.memo2020.R
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.view.ViewFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewFragmentViewmodel(application: Application) : AndroidViewModel(application) {

    val dao = MemoDatabase.get(application).memoDao()

    val memoItem: MutableLiveData<Memo?> = MutableLiveData()
    val imgList: MutableLiveData<ArrayList<String>> = MutableLiveData()


    // * set memo item (received as argument)
    fun setMemoItem(memo: Memo? = null, position: Int? = null) {
        memoItem.value = memo
        imgList.value = memo?.getImageList() ?: ArrayList()

    }


    // move to EditFragment
    fun moveEditFragment(view: View) {
        val action = ViewFragmentDirections.actionViewFragmentToEditFragment(memoItem.value)
        Navigation.findNavController(view).navigate(action)
    }


    fun delete(activity: Activity) {
        if (memoItem.value == null) return
        delete(activity, memoItem.value!!.id)
    }

    private fun delete(activity: Activity, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }.let {
            Toast.makeText(activity, activity.getString(R.string.memo_delete), Toast.LENGTH_SHORT).show()
            Navigation.findNavController(activity, R.id.main_fragment).navigateUp()

        }
    }


}


