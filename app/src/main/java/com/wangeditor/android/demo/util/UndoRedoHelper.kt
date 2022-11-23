package com.wangeditor.android.demo.util

import androidx.lifecycle.MutableLiveData
import com.wangeditor.android.WangRichEditor
import com.wangeditor.android.WangRichEditor.OnTextChangeListener
import java.util.*


class UndoRedoHelper(
    /**
     * The edit text.
     */
    private val editor: WangRichEditor
) {
    /**
     * Is undo/redo being performed? This member signals if an undo/redo
     * operation is currently being performed. Changes in the text during
     * undo/redo are not recorded because it would mess up the undo history.
     */
    private var mIsUndoOrRedo = false

    /**
     * The edit history.
     */
    private val mEditHistory: EditHistory

    /**
     * The change listener.
     */
    private val mChangeListener: EditTextChangeListener
    private val defText = ""
    private var currentText = ""
    val isCanUndo: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isCanRedo: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    // =================================================================== //
    /**
     * Create a new TextViewUndoRedo and attach it to the specified TextView.
     *
     * @param editor The text view for which the undo/redo is implemented.
     */
    init {
        mEditHistory = EditHistory()
        mChangeListener = EditTextChangeListener()
        editor.addOnTextChangeListener(mChangeListener)
    }

    /**
     * Set the maximum history size. If size is negative, then history size is
     * only limited by the device memory.
     */
    fun setMaxHistorySize(maxHistorySize: Int) {
        mEditHistory.setMaxHistorySize(maxHistorySize)
    }

    /**
     * Clear history.
     */
    fun clearHistory() {
        mEditHistory.clear()
    }

    /**
     * Can undo be performed?
     */
    fun getCanUndo(): Boolean {
        return mEditHistory.mmPosition > 1
    }

    /**
     * Perform undo.
     */
    fun undo() {
        if (mIsUndoOrRedo) return
        val edit: EditItem? = mEditHistory.getPrevious()
        if (edit == null) {
            editor.html = defText
            return
        }
        mIsUndoOrRedo = true
        currentText = edit.text
        editor.html = edit.text
    }

    /**
     * Can redo be performed?
     */
    fun getCanRedo(): Boolean {
        return mEditHistory.mmPosition < mEditHistory.mmHistory.size
    }

    /**
     * Perform redo.
     */
    fun redo() {
        if (mIsUndoOrRedo) return
        val edit: EditItem = mEditHistory.getNext() ?: return
        mIsUndoOrRedo = true
        currentText = edit.text
        editor.html = edit.text
    }

    fun initDefContent(info: String?) {
        if (info == null) {
            mEditHistory.add(EditItem(""))
            return
        }
        mEditHistory.add(EditItem(info))
    }
    // =================================================================== //
    /**
     * Keeps track of all the edit history of a text.
     */
    private inner class EditHistory {
        /**
         * The position from which an EditItem will be retrieved when getNext()
         * is called. If getPrevious() has not been called, this has the same
         * value as mmHistory.size().
         */
        internal var mmPosition = 0

        /**
         * Maximum undo history size.
         */
        private var mmMaxHistorySize = -1

        /**
         * The list of edits in chronological order.
         */
        internal val mmHistory = LinkedList<EditItem>()

        /**
         * Clear history.
         */
        internal fun clear() {
            mmPosition = 0
            mmHistory.clear()
        }

        /**
         * Adds a new edit operation to the history at the current position. If
         * executed after a call to getPrevious() removes all the future history
         * (elements with positions >= current history position).
         */
        internal fun add(item: EditItem) {
            while (mmHistory.size > mmPosition) {
                mmHistory.removeLast()
            }
            mmHistory.add(item)
            mmPosition++
            if (mmMaxHistorySize >= 0) {
                trimHistory()
            }
        }

        /**
         * Set the maximum history size. If size is negative, then history size
         * is only limited by the device memory.
         */
        internal fun setMaxHistorySize(maxHistorySize: Int) {
            mmMaxHistorySize = maxHistorySize
            if (mmMaxHistorySize >= 0) {
                trimHistory()
            }
        }

        /**
         * Trim history when it exceeds max history size.
         */
        private fun trimHistory() {
            while (mmHistory.size > mmMaxHistorySize) {
                mmHistory.removeFirst()
                mmPosition--
            }
            if (mmPosition < 0) {
                mmPosition = 0
            }
        }

        /**
         * Traverses the history backward by one position, returns and item at
         * that position.
         */
        internal fun getPrevious(): EditItem? {
            if (mmPosition == 0) {
                return null
            }
            mmPosition--
            return mmHistory[mmPosition - 1]
        }

        /**
         * Traverses the history forward by one position, returns and item at
         * that position.
         */
        internal fun getNext(): EditItem? {
            if (mmPosition >= mmHistory.size) {
                return null
            }
            val item = mmHistory[mmPosition]
            mmPosition++
            return item
        }

        internal fun getCurrent(): EditItem? {
            return if (mmPosition == 0) {
                null
            } else mmHistory[mmPosition - 1]
        }
    }

    /**
     * Represents the changes performed by a single edit operation.
     */
    private inner class EditItem(var text: String)

    /**
     * Class that listens to changes in the text.
     */
    private inner class EditTextChangeListener : OnTextChangeListener {
        override fun onTextChange(text: String?) {
            if (text == null) return
            if (text == "<p><br></p>" && mEditHistory.mmHistory.size <= 1) return
            if (mIsUndoOrRedo) {
                if (text == "<p><br></p>" && text != currentText) {
                    editor.html = currentText
                    return
                }
                mIsUndoOrRedo = false
            } else {
                if (mEditHistory.getCurrent() == null || mEditHistory.getCurrent()!!.text != text) {
                    mEditHistory.add(EditItem(text))
                }
            }
            isCanUndo.postValue(getCanUndo())
            isCanRedo.postValue(getCanRedo())
        }
    }
}