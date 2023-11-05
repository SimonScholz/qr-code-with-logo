package io.github.simonscholz.observables

import org.eclipse.core.databinding.observable.Realm
import javax.swing.SwingUtilities

class SwingRealm : Realm() {

    init {
        setDefault(this)
    }
    override fun isCurrent(): Boolean =
        SwingUtilities.isEventDispatchThread()

    override fun syncExec(runnable: Runnable) =
        SwingUtilities.invokeAndWait(runnable)

    override fun asyncExec(runnable: Runnable) =
        SwingUtilities.invokeLater(runnable)
}
