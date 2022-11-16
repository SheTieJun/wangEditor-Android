package com.wangeditor.android.demo.util;

import androidx.lifecycle.MutableLiveData;
import com.wangeditor.android.WangRichEditor;
import java.util.LinkedList;
import org.jetbrains.annotations.Nullable;

public class UndoRedoHelper {
    /**
     * Is undo/redo being performed? This member signals if an undo/redo
     * operation is currently being performed. Changes in the text during
     * undo/redo are not recorded because it would mess up the undo history.
     */
    private boolean mIsUndoOrRedo = false;

    /**
     * The edit history.
     */
    private EditHistory mEditHistory;

    /**
     * The change listener.
     */
    private EditTextChangeListener mChangeListener;

    /**
     * The edit text.
     */
    private WangRichEditor editor;
    private String  defText = "";

    public final MutableLiveData<Boolean> isCanUndo = new MutableLiveData(false);
    public final MutableLiveData<Boolean> isCanRedo = new MutableLiveData(false);
    // =================================================================== //

    /**
     * Create a new TextViewUndoRedo and attach it to the specified TextView.
     *
     * @param editor The text view for which the undo/redo is implemented.
     */
    public UndoRedoHelper(WangRichEditor editor) {
        this.editor = editor;
        mEditHistory = new EditHistory();
        mChangeListener = new EditTextChangeListener();
        this.editor.addOnTextChangeListener(mChangeListener);
    }


    /**
     * Set the maximum history size. If size is negative, then history size is
     * only limited by the device memory.
     */
    public void setMaxHistorySize(int maxHistorySize) {
        mEditHistory.setMaxHistorySize(maxHistorySize);
    }

    /**
     * Clear history.
     */
    public void clearHistory() {
        mEditHistory.clear();
    }

    /**
     * Can undo be performed?
     */
    public boolean getCanUndo() {
        return (mEditHistory.mmPosition > 1);
    }

    /**
     * Perform undo.
     */
    public void undo() {
        if (mIsUndoOrRedo) return;
        EditItem edit = mEditHistory.getPrevious();
        if (edit == null) {
            editor.setHtml(defText);
            return;
        }
        mIsUndoOrRedo = true;
        editor.setHtml(edit.text);
    }

    /**
     * Can redo be performed?
     */
    public boolean getCanRedo() {
        return (mEditHistory.mmPosition < mEditHistory.mmHistory.size());
    }

    /**
     * Perform redo.
     */
    public void redo() {
        if (mIsUndoOrRedo) return;
        EditItem edit = mEditHistory.getNext();
        if (edit == null) {
            return;
        }
        mIsUndoOrRedo = true;
        editor.setHtml(edit.text);
    }

    public void initDefContent(@Nullable String info) {
        mEditHistory.add(new EditItem(info));
    }


    // =================================================================== //

    /**
     * Keeps track of all the edit history of a text.
     */
    private final class EditHistory {

        /**
         * The position from which an EditItem will be retrieved when getNext()
         * is called. If getPrevious() has not been called, this has the same
         * value as mmHistory.size().
         */
        private int mmPosition = 0;

        /**
         * Maximum undo history size.
         */
        private int mmMaxHistorySize = -1;

        /**
         * The list of edits in chronological order.
         */
        private final LinkedList<EditItem> mmHistory = new LinkedList<EditItem>();

        /**
         * Clear history.
         */
        private void clear() {
            mmPosition = 0;
            mmHistory.clear();
        }

        /**
         * Adds a new edit operation to the history at the current position. If
         * executed after a call to getPrevious() removes all the future history
         * (elements with positions >= current history position).
         */
        private void add(EditItem item) {
            while (mmHistory.size() > mmPosition) {
                mmHistory.removeLast();
            }
            mmHistory.add(item);
            mmPosition++;

            if (mmMaxHistorySize >= 0) {
                trimHistory();
            }
        }

        /**
         * Set the maximum history size. If size is negative, then history size
         * is only limited by the device memory.
         */
        private void setMaxHistorySize(int maxHistorySize) {
            mmMaxHistorySize = maxHistorySize;
            if (mmMaxHistorySize >= 0) {
                trimHistory();
            }
        }

        /**
         * Trim history when it exceeds max history size.
         */
        private void trimHistory() {
            while (mmHistory.size() > mmMaxHistorySize) {
                mmHistory.removeFirst();
                mmPosition--;
            }

            if (mmPosition < 0) {
                mmPosition = 0;
            }
        }

        /**
         * Traverses the history backward by one position, returns and item at
         * that position.
         */
        private EditItem getPrevious() {
            if (mmPosition == 0) {
                return null;
            }
            mmPosition--;
            return mmHistory.get(mmPosition-1);
        }

        /**
         * Traverses the history forward by one position, returns and item at
         * that position.
         */
        private EditItem getNext() {
            if (mmPosition >= mmHistory.size()) {
                return null;
            }
            EditItem item = mmHistory.get(mmPosition);
            mmPosition++;
            return item;
        }

        private EditItem getCurrent() {
            if (mmPosition == 0) {
                return null;
            }
            return mmHistory.get(mmPosition - 1);
        }
    }

    /**
     * Represents the changes performed by a single edit operation.
     */
    private final class EditItem {

        public String text;

        public EditItem(String text) {
            this.text = text;
        }
    }

    /**
     * Class that listens to changes in the text.
     */
    private final class EditTextChangeListener implements WangRichEditor.OnTextChangeListener {

        @Override
        public void onTextChange(String text) {
            if (text.equals("<p><br></p>") && mEditHistory.mmHistory.size() <=1) return;
            if (mIsUndoOrRedo) {
                mIsUndoOrRedo = false;
            } else {
                if (mEditHistory.getCurrent() == null || !mEditHistory.getCurrent().text.equals(text)) {
                    mEditHistory.add(new EditItem(text));
                }
            }
            isCanUndo.postValue(getCanUndo());
            isCanRedo.postValue(getCanRedo());
        }
    }
}
