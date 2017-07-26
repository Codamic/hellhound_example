(ns hh-example.system)

(def webserver {:hellhound.component/name :webserver
                :hellhound.component/factory (fn [])
                :hellhound.component/dependencies [:logger]
                :hellhound.component/in [:]
                :hellhound.component/params {:router ::handler}})

(def components
  [])

(def system
  {:components})
