(ns systems.simple-system3
  (:require
   [manifold.stream :as s]
   [hellhound.system :as system]
   [hellhound.component :as hcomp]))

;; The start function of the component 1 which will assign
;; to the component later in the component map.
;; The first argument is the componet map itself and the argument
;; is the context map which contains extra info about the context
;; which this component is running on.
;; As you already know start function of a component should return
;; a component map.
;;
;; This component basically applies function `inc` on any incoming
;; value from its input and put it to its output.
(defn start-fn1
  [component context]
  (let [input  (hcomp/input component)
        output (hcomp/output component)]
    (s/connect input output))
  component)

;; Stop function of all the components. It should returns a component
;; map.
(defn stop-fn
  [component]
  (println "Stopping")
  component)

;; Start function of component-2.
(defn start-fn2
  [component context]
  ;; Gets the input/output of the current component
  (let [input (hcomp/input component)]
    (s/consume #(println "Odd: " %) input)
    component))

;; Start function of component-2.
(defn start-fn3
  [component context]
  (let [input (hcomp/input component)]
    (s/consume #(println "Even: " %) input)
    component))

;; Defining all three components needed for this system to work. Please notice
;; that we didn't defined any dependencies for these components. Pluging them
;; to each other using a workflow catalog is a different story from component
;; dependencies. We only need to define a component as a dependency if the second
;; component use the first one directly in its start or stop function.
(def component-1 (hcomp/make-component ::component-1 start-fn1 stop-fn))
(def component-2 (hcomp/make-component ::component-2 start-fn2 stop-fn))
(def component-3 (hcomp/make-component ::component-3 start-fn3 stop-fn))

;; Defines a system with a linear workflow. In this case **HellHound** starts all
;; the components in the system and then wires up components IO based on the
;; desciption given by the `:workflow` key of the system.
;;
;; In this system, the workflow would be like:
;; DATA ---> component-1 ---> component-2 ---> component-3
;;
;; Component 3 don't have any output stream. But it can have one.
(def simple-system
  {:components [component-2 component-1 component-3]
   :workflow [[::component-1 odd? ::component-2]
              [::component-1 even? ::component-3]]})

;; The main function of this namespace. You can run this
;; example by issuing following command:
;;
;; $ lein run -m systems.simple-system2/main
(defn main
  []
  ;; Setting the `simple-system` as the default system of the application
  (system/set-system! simple-system)
  ;; Start the default system
  (system/start!)

  ;; Gets a compone1nt with the name from the default system.
  (let [component1 (system/get-component ::component-1)
        ;; Gets the input of the component1
        input      (hcomp/input component1)]

    (-> [1 2 3 4 5 6]
        ;; Converts the vector to a stream source
        (s/->source)
        ;; Connects the stream source to the input of component1
        (s/connect input))
    ;; (println "xczczxc")
    ;; (println component1)
    ;; (println input)
    (Thread/sleep 3000)
    (println "Done.")))
