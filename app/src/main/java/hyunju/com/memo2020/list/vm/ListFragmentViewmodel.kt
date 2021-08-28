package hyunju.com.memo2020.list.vm

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import hyunju.com.memo2020.R
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.list.view.ListFragmentDirections
import hyunju.com.memo2020.model.Memo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ListFragmentViewmodel(application: Application) : AndroidViewModel(application) {

    private var disposable : Disposable? = null

    val dao = MemoDatabase.get(application).memoDao()
    var allMemos: LiveData<PagedList<Memo>> = LivePagedListBuilder(
            dao.getAllMemoByDate(),
            8
    ).build()

    fun moveEditFragment(view: View, memoItem: Memo? = null) {
        val action =
                ListFragmentDirections.actionListFragmentToEditFragment(memoItem)

        Navigation.findNavController(view).navigate(action)
    }

    fun showSelectDialog(context: Context, activity: Activity, view: View, memoItem: Memo?) {
        val dialogView = activity.layoutInflater.inflate(R.layout.select_dialog, null)

        val builder = AlertDialog.Builder(context)
        val dialog = builder.setView(dialogView).create()

        val deleteBtn = dialogView.findViewById<ImageButton>(R.id.select_dialog_delete)
        val editBtn = dialogView.findViewById<ImageButton>(R.id.select_dialog_edit)

        deleteBtn.setOnClickListener {
            dialog.dismiss()
            delete(activity, memoItem!!)
        }
        editBtn.setOnClickListener {
            dialog.dismiss()
            moveEditFragment(view, memoItem)
        }

        dialog.show()
    }

    private fun delete(activity: Activity, memo: Memo) {
        disposable = Single.just(memo)
            .subscribeOn(Schedulers.io())
            .map { dao.delete(it) > 0}
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ isSuccess ->
                if (isSuccess) {
                    Toast.makeText(activity, activity.getString(R.string.memo_delete), Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(activity, R.id.main_fragment).navigateUp()
                }
            }, {
                it.printStackTrace()
            })
    }

    fun onDestroyViewModel() {
        disposable?.dispose()
    }


}


