;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname |ue4-vorlage 2|) (read-case-sensitive #t) (teachpacks ((lib "image.rkt" "teachpack" "2htdp"))) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ((lib "image.rkt" "teachpack" "2htdp")) #f)))
;; The alphabet we use is encoded in eight bits and the numbers correspond to ASCII symbols.
;; ASCII number 27 "\e" is used as termination symbol.

;;################## 6.2 #################

;;convert-to-base-four: number -> (listof number)
;;Convertiert eine Zahl aus dem Dezimalsystem in eine Zahl im Vierersystem
;;Beispiel: (convert-to-base-four 12) ergibt (list 3 0)
(define (convert-to-base-four num)
  (cond [(= num 0) '()]
        ;;Else - Rufe Die Procedur rekursiv auf und hänge das berechnete modulo hinten an die Liste an.
        [else (append (convert-to-base-four (quotient num 4)) (list (remainder num 4)))])
)

(check-expect (convert-to-base-four 10) (list 2 2))
(check-expect (convert-to-base-four 100) (list 1 2 1 0))

;;################### 6.3 #################


;;string->encodeable: string -> (listof (listof number))
;;Wandelt einen string (in eine liste von dezimalzahlen um und diese wiederum) in eine liste von zahlen im 4er System um. Sollte der String leer sein "" wird eine leere liste zurückgegeben
;;Beispiel: (string->encodeable "") ergibt '()
(define (string->encodeable s)
  (map convert-to-base-four (map string->int (explode s)))
)
(check-expect (string->encodeable "fop") (list (list 1 2 1 2) (list 1 2 3 3) (list 1 3 0 0)))
(check-expect (string->encodeable "FOP") (list (list 1 0 1 2) (list 1 0 3 3) (list 1 1 0 0)))



;;convert-to-dezimal: (listof number) number -> number
;;Berechnet eine Liste von Zahlen im 4er System ins Dezimalsystem um.
;;Beispiel: (convert-to-dezimal (list 1 1)) ergibt 5
(define (convert-to-dezimal lon)
  (local
    (
     ;;**: number number -> number
     ;;Gibt base hoch potenz aus.
     ;;Beispiel: (** 4 0) ergibt 1
     (define (** base potenz)
       (cond [(= potenz 0) 1]
             [else (* base (** base (- potenz 1)))]
       )
     )
     ;;convert-to-dez: (listof number) number
     ;;;;Berechnet eine Liste von Zahlen im 4er System ins Dezimalsystem um.
     ;;Beispiel: (convert-to-dezimal (list 1 1) 0) ergibt 5
     (define (convert-to-dez lon speicher)
       (cond [(empty? lon) 0]
        [else (+ (* (first lon) (** 4 speicher)) (convert-to-dez (rest lon) (+ speicher 1)))])
       ))
    (convert-to-dez (reverse lon) 0)
  )
)
(check-expect (convert-to-dezimal (list 2 2)) 10)
(check-expect (convert-to-dezimal (list 1 2 1 0)) 100)


;;encodeable->string (listof (listof number)) -> string
;;Wandelt einen string (in eine liste von dezimalzahlen um und diese wiederum) in eine liste von zahlen im 4er System um. Sollte der String leer sein "" wird eine leere liste zurückgegeben
;;Beispiel: (string->encodeable "") ergibt '()
(define (encodeable->string lon)
  (implode (map int->string (map convert-to-dezimal lon)))
)
(check-expect (encodeable->string (list (list 1 2 1 2) (list 1 2 3 3) (list 1 3 0 0))) "fop")
(check-expect (encodeable->string (list (list 1 0 1 2) (list 1 0 3 3) (list 1 1 0 0))) "FOP")



; load-image: string -> (list of color)
;; Loads the image given by path-to-image and converts it to a list of color.
;; Example: (load-image "example.png") -> (list (color 128 255 32 255) ...)
(define (load-image path-to-image)
  (image->color-list (bitmap/file path-to-image))
  )

;; store-image: (list of color) string number number -> boolean
;; Saves the image in PNG format at path-to-image.
;; loc: List of color which is supposed be stored in PNG format.
;; path-to-image: Relative path to the location where the image should be saved.
;; width: The width of the image.
;; heigth: The height of the image. 
(define (store-image loc path-to-image width height)
  (save-image (color-list->bitmap loc width height) path-to-image)
  )


;;################ 6.4 ###########



;(check-expect (index-of-element-in-abc "a") 0)
;(check-expect (index-of-element-in-abc "z") 25)

;;get-list-of-indexes: (listof symbol) -> (listof number)
;;Gibt für jedes Element (Buchstabe) der Liste die Indexzahl in einer Liste aus 
;;(check-expect (get-list-of-indexes (list "b")) (list 1))
(define (get-list-of-indexes los)
  (local
    ;;index-of-element-in-abc: string -> number
    ;;Gibt den Index eines Buchstaben in ABC aus
    ;;Beispiel: (index-of-element-in-abc "b") ergibt 1
    ((define (index-of-element-in-abc element)
       (local (
          ;;Liste die alle buchstaben des alphabets als string enthält
          (define abc (list "a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m" "n" "o" "p" "q" "r" "s" "t" "u" "v" "w" "x" "y" "z"))
          ;;index-of-element-in-abc: string -> number
          ;;Zählt die index variable hoch bis das element gleich dem element aus der liste ist
          ;;Beispiel: (index-of-element abc "a" 0) ergibt 1
          (define (index-of-element los e counter)
            ;;Wenn liste leer ist (auch abbruchbedingung) rechne + (-1) damit die indexzahlen im bereich 0-25 liegen
            (cond [(empty? los) -1]
                  [(string-ci=? (first los) e) counter]
                  [else (index-of-element (rest los) e (+ counter 1))]))
         )
         (index-of-element abc element 0)
         )
    ))
    (cond [(empty? los) '()]
        [else (append (list (index-of-element-in-abc (first los))) (get-list-of-indexes (rest los)))])
  )
)
(check-expect (get-list-of-indexes (list "a")) (list 0))
(check-expect (get-list-of-indexes (list "a" "z")) (list 0 25))


;;convert-to-base-four-4stellen: (listof number) -> (listof number)
;;Hängt an eine Liste von Zahlen 0en an, bis die Liste 4 Elemente enthält
;;Beispiel: (convert-to-base-four-4stellen (list 1 1 1)) ergibt (list 0 1 1 1)
(define (convert-to-base-four-4stellen lon)
  (cond [(>= (length lon) 4) lon]
        [else (convert-to-base-four-4stellen (append (list 0) lon))])
)
(check-expect (convert-to-base-four-4stellen (list 1)) (list 0 0 0 1))
(check-expect (convert-to-base-four-4stellen (list 1 1 1 1)) (list 1 1 1 1))


;;steganographie-enc (listof color) string string -> (listof color)
;;Bindet eine nachricht m mit einem Passwort k in eine Liste von Color ein. An den einzelnen Indexzahlen von k wird je ein Buchstabe eingesetzt
;;Keine Beispiele laut Aufgabenstellung nötig
(define (steganographie-enc loc m k)
  (local (
          ;;message: string -> (listof (listof number))
          ;;Hängt an die message m von steganographie-enc ein \e an und wandelt m in eine Liste von 4er System Zahlen um
          ;;Beispiel: (string->encodeable (string-append "a" "\e")) ergibt (list (list 1 2 0 1) (list 1 2 3))
          (define message (string->encodeable (string-append m "\e")))
          ;;key: string -> (listof number)
          ;;Gibt eine < sortierte Liste mit den Indexzahlen der einzelnen Buchstaben von k aus
          ;;Beispiel: (sort (get-list-of-indexes (explode "Hallo")) <)) ergibt (list 0 7 11 11 14)
          (define key (sort (get-list-of-indexes (explode k)) <))
          ;;update-color-data: color (listof number)
          ;;Fügt an die letzte stelle jedes Farbwertes von color einen der Werte von newData an
          ;;Beispiel: (update-color-data (make-color 255 255 255 255) (list 0 0 0 0)) ergibt (make-color 252 252 252 252)
          (define (update-color-data colorData newData)
            ;;Benötigt keine Überprüfung ob newData 4 Elemente hat, da convert-to-base-four-4stellen aufgerufen wird
            (make-color (convert-to-dezimal (list (first (convert-to-base-four (color-red colorData)))
                                                              (second (convert-to-base-four (color-red colorData)))
                                                              (third (convert-to-base-four (color-red colorData)))
                                                              (first (convert-to-base-four-4stellen newData))))
                                    (convert-to-dezimal (list (first (convert-to-base-four (color-green colorData)))
                                                              (second (convert-to-base-four (color-green colorData)))
                                                              (third (convert-to-base-four (color-green colorData)))
                                                              (second (convert-to-base-four-4stellen newData))))
                                    (convert-to-dezimal (list (first (convert-to-base-four (color-blue colorData)))
                                                              (second (convert-to-base-four (color-blue colorData)))
                                                              (third (convert-to-base-four (color-blue colorData)))
                                                              (third (convert-to-base-four-4stellen newData))))
                                    (convert-to-dezimal (list (first (convert-to-base-four (color-alpha colorData)))
                                                              (second (convert-to-base-four (color-alpha colorData)))
                                                              (third (convert-to-base-four (color-alpha colorData)))
                                                              (fourth (convert-to-base-four-4stellen newData)))))
          )

          
          ;;encode (listof color) (listof (listof number)) (listof number) (listof number) number -> (listof color)
          ;;Fügt einzelne Buchstaben von message in eine liste von color an den einzelnen password stellen ein
          ;;Beispiel: Beispiel laut Aufgabenstellung nicht nötig!
          (define (encode loc message password completePassword counter)
            (cond [(empty? loc) '()]
                  ;;Wenn das password leer ist, ist ein 26pixel block abgearbeitet d.h. rekursion mit completePassword als das neue password
                  [(empty? password) (encode loc message completePassword completePassword counter)]
                  [(empty? message) loc]
                  [else (if (= (first password) (remainder counter 26))
                    ;;Der Index von password stimmt mit dem aktuellen Pixel überein. Also wird dieses struct bearbeitet
                    (append (list (update-color-data (first loc) (first message))) (encode (rest loc) (rest message) (rest password) completePassword (+ counter 1)))
                   ;;else
                    (append (list (first loc)) (encode (rest loc) message password completePassword (+ counter 1)))
                    )
                  ])
          )
          ;;check-if-message-fits-image (listof color) (listof (listof number)) (listof number) -> Bool
          ;;Überprüft ob die nachricht mit dem gegebenen Password in loc passt
          ;;(check-if-message-fits-image (list (make-color 255 255 255 255)) (listof (listof 1 1 1 1) (listof 1 1 1 1)) (listof 1 2 3)) ergibt false
          (define (check-if-message-fits-image loc m k)
            (>= (length loc) (* 26 (ceiling (/ (length m) (length k)))))
          ) 
        )
    (if (check-if-message-fits-image loc message key)
        (encode loc message key key 0)
        (error 'steganographie-enc "Nachricht passt nicht ins Bild")
   )
 )
)
;;Keine Tests laut Aufgabenstellung nötig


;;################ 6.5 ##################


;;steganographie-dec: (listof color) string -> string
;;Gibt den in der liste von color enthaltenen Text aus. 
;;Beispiel: ---
(define (steganographie-dec loc k)
  (local
    (
     ;;
     (define key (sort (get-list-of-indexes (explode k)) <))
     ;;get-message-data: (listof color) (listof number) (listof number) number -> (listof color)
     ;;Gibt eine Liste von color aus mit den Color-elementen, die an den einzelnen password stellen stehen
     ;;Beispiel: Laut aufgabenstellung kein Beispiel notwendig
     (define (get-message-data loc password completePassword counter)
       (cond [(empty? loc) '()]
             [(empty? password) (get-message-data loc completePassword completePassword counter)]
             [else (if (= (first password) (remainder counter 26))
;;############################################################################################################################################
                       (append (list (first loc)) (get-message-data (rest loc) (rest password) completePassword (+ counter 1)))
                       ;;else
                       (append '() (get-message-data (rest loc) password completePassword (+ counter 1)))
                   )
             ])
     )
     ;;convert-loc-to-lon: (listof color) -> (listof (listof number))
     ;;Konvertiert eine Liste von color in eine Liste von Zahlen im 4er System
     ;;Beispiel: (convert-loc-to-lon (list (make-color 255 255 255 255))) ergibt (list (list 3 3 3 3))
     (define (convert-loc-to-lon loc)
       (cond [(empty? loc) '()]
             [else (append (list (list (fourth (convert-to-base-four-4stellen (convert-to-base-four (color-red (first loc)))))
                                       (fourth (convert-to-base-four-4stellen (convert-to-base-four (color-green (first loc)))))
                                       (fourth (convert-to-base-four-4stellen (convert-to-base-four (color-blue (first loc)))))
                                       (fourth (convert-to-base-four-4stellen (convert-to-base-four (color-alpha (first loc)))))
                                       ))
                           (convert-loc-to-lon (rest loc)))]
       )
     )
    )
    
    (encodeable->string (convert-loc-to-lon (get-message-data loc key key 0)))
  )
)


;(steganographie-dec (load-image "steganographie.png") "FOPRules")


;ERGEBNIS 6.6
;;"Suchen Sie sich ein Bild um Ihren Namen, wie er in Moodle steht, in diesem Bild zu verstecken. Benutzen Sie als Passwort \"encrypt\". Geben Sie das Bild und Ihren Code als ein Zip-Archive ab.\e

;;create-squared-image: number -> (listof color)
;;Erstellt eine Liste von Color (ein Bild mit der seiten höhe/breite pixelWidth)
;;Beispiel: (create-squared-image 16) ergibt eine Liste mit 256 * (make-color 255 255 255 255)
(define (create-squared-image pixelWidth)
  (local ((define totalpixel (sqr pixelWidth))
          (define (create-image pixelCount)
            (cond [(= pixelCount 0) '()]
                  [else (append (list (make-color 255 255 255 255)) (create-image (- pixelCount 1)))])
            ))
    (create-image totalpixel)
  )
)
(check-expect (create-squared-image 1) (list (make-color 255 255 255 255)))
(check-expect (create-squared-image 2) (list (make-color 255 255 255 255) (make-color 255 255 255 255) (make-color 255 255 255 255) (make-color 255 255 255 255)))

;(store-image (steganographie-enc (create-squared-image 256) "Tobias Werner" "encrypt") "/Dateipfad" 256 256)

;;(steganographie-dec (load-image "fop-bild.png") "encrypt")