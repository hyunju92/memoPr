package hyunju.com.memo2020.list.vm

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.paging.PagedList
import hyunju.com.memo2020.R
import hyunju.com.memo2020.list.view.ListFragmentDirections
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.model.MemoRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ListFragmentViewmodel(private val repository: MemoRepository, private val fragment: Fragment, private val context: Context) {

    private var disposable : Disposable? = null

    // LiveData
    var allMemos: LiveData<PagedList<Memo>> = repository.allMemos

    fun moveEditFragment(memoItem: Memo? = null) {
        val action =
                ListFragmentDirections.actionListFragmentToEditFragment(memoItem)

        Navigation.findNavController(fragment.requireView()).navigate(action)
    }

    fun showSelectDialog(memoItem: Memo?) {
        val dialogView = fragment.requireActivity().layoutInflater.inflate(R.layout.select_dialog, null)

        val builder = AlertDialog.Builder(fragment.requireActivity())
        val dialog = builder.setView(dialogView).create()

        val deleteBtn = dialogView.findViewById<ImageButton>(R.id.select_dialog_delete)
        val editBtn = dialogView.findViewById<ImageButton>(R.id.select_dialog_edit)

        deleteBtn.setOnClickListener {
            dialog.dismiss()
            delete(fragment.requireActivity(), memoItem!!)
        }
        editBtn.setOnClickListener {
            dialog.dismiss()
            moveEditFragment(memoItem)
        }

        dialog.show()
    }

    private fun delete(activity: Activity, memo: Memo) {
        disposable = repository.delete(memo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Toast.makeText(activity, activity.getString(R.string.memo_delete), Toast.LENGTH_SHORT).show()
                Navigation.findNavController(activity, R.id.main_fragment).navigateUp()
            }, {
                it.printStackTrace()
            })
    }

    fun onDestroyViewModel() {
        disposable?.dispose()
    }


}


