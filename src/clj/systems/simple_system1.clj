(ns systems.simple-system1
  (:require [hellhound.system :as system]
            [hellhound.component :as hcomp]))

;; The start function of the component 1 which will assign
;; to the component later in the component map.
;; The first argument is the componet map itself and the argument
;; is the context map which contains extra info about the context
;; which this component is running on.
;; As you already know start function of a component should return
;; a component map.
(defn start-fn1
  [component context]
  (println "Starting Component 1...")
  (assoc component :something-in "Hello World"))


;; Stop function of the component1. It should returns a component
;; map.
(defn stop-fn1
  [component]
  (println "Stopping component 2...")
  component)

;; Start function of component-2.
(defn start-fn2
  [component context]
  ;; Getsthe first dependency component. In the order which defined in the
  ;; component map `:depends-on`
  (let [component1 (first (:dependencies context))
        ;; Gets the same component by it's name instead.
        component1-with-name (:simple-system/component-1 (:dependencies-map context))]
    (println "Starting Component 2...")
    ;; Use a value defined in component1
    (println (:something-in component1))
    (println (:something-in component1-with-name))
    component))

;; Stop function of component2
(defn stop-fn2
  [component]
  (println "Stopping component 2...")
  component)

;; A component map which defines the `:simple-system/component-1` component
;; Intentionally we defined this component by defining a map directly.
;; But you can use `make-component` function as a shortcut.
(def component-1 {:hellhound.component/name :simple-system/component-1
                  :hellhound.component/start-fn start-fn1
                  :hellhound.component/stop-fn  stop-fn1})

;; We used `make-component` to define a component called `:simple-system/component-2`.
;; It basically returns a map like we had in component-1 with the given details.
;; Note: the last argument is the dependency vector of the component that exactly is
;; going to be the `:depends-on` key in the component map.
(def component-2 (hcomp/make-component :simple-system/component-2 start-fn2 stop-fn2 [:simple-system/component-1]))

;; A very simple `system` defination which does not have any workflow.
;; Please not that the order of components in `:components` vector is
;; NOT important at all.
(def simple-system
  {:components [component-2 component-1]})

;; The main function of this namespace. You can run this
;; example by issuing following command:
;;
;; $ lein run -m systems.simple-system1/main
(defn main
  []
  ;; Setting the `simple-system` as the default system of the application
  (system/set-system! simple-system)
  ;; Start the default system
  (system/start!))
