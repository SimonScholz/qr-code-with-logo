package io.github.simonscholz.observables

import org.eclipse.core.databinding.observable.Diffs
import org.eclipse.core.databinding.observable.value.AbstractObservableValue
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.Document
import javax.swing.text.JTextComponent

class DocumentObservable(
    private val document: Document,
) : AbstractObservableValue<String>() {
    private var documentValue = document.getText(0, document.length)

    constructor(jTextComponent: JTextComponent) : this(jTextComponent.document)

    init {
        document.addDocumentListener(
            object : DocumentListener {
                override fun changedUpdate(e: DocumentEvent) {
                    val oldValue = documentValue
                    documentValue = doGetValue()
                    fireValueChange(Diffs.createValueDiff(oldValue, documentValue))
                }

                override fun insertUpdate(e: DocumentEvent) {
                    val oldValue = documentValue
                    documentValue = doGetValue()
                    fireValueChange(Diffs.createValueDiff(oldValue, documentValue))
                }

                override fun removeUpdate(e: DocumentEvent) {
                    val oldValue = documentValue
                    documentValue = doGetValue()
                    fireValueChange(Diffs.createValueDiff(oldValue, documentValue))
                }
            },
        )
    }

    override fun getValueType(): Any = String::class.java

    override fun doGetValue(): String = document.getText(0, document.length)

    override fun doSetValue(value: String?) {
        if (documentValue !== value) {
            document.remove(0, document.length)
            document.insertString(0, value, null)
        }
    }
}
