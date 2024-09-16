package org.skainnet.io.named

import org.skainet.nn.Module
import org.skainet.nn.NamedParameter

class ModelsNamedParamsUpdater(private val namedParamsLoader: NamedParamsLoader) {
    fun update(model: Module) {
        namedParamsLoader.load { loadedNamedParameter ->
            model.findNamedParameterBy(loadedNamedParameter.name)?.let { parameter ->
                parameter.value = loadedNamedParameter.value
            }
        }
    }

    private fun Module.findNamedParameterBy(fullName: String): NamedParameter? {

        fun findNamedParameterBy(fullName: String, module: Module): NamedParameter? {
            if (fullName.startsWith(module.name)) {
                module.params.find { param -> fullName.contains(param.name) }?.let {
                    return it
                }
            }
            for (subModule in module.modules) {
                findNamedParameterBy(fullName, subModule)?.let {
                    return it
                }
            }
            return null
        }

        return findNamedParameterBy(fullName, this)
    }
}