;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-abbr-reader.ss" "lang")((modname FOPÜ126.10) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))
;; Leonard Bongard
;; Exercise 7.1
;; distance-centers:number number : M1 ; number number : M2 ; M1 M2 Abstand
;; Explanation: der Abstand zwischen M1 und M2 wird gemessen
;; Example: (distance-centers 1 4 6 5)= d(M1,M2)= (sqrt(+(sqr(- 1 6 ))(sqr(- 4 5)))) = 5.09901

(define (distance-centers x1 y1 x2 y2)
  (sqrt(+(sqr(- x1 x2 ))(sqr(- y1 y2))))
  )

;; Tests

> (check-within (distance-centers 1 4 6 5) 5.09901 0.0001)
The test passed!
> (check-within (distance-centers 1 1 1 1) 0 0.0001)
                
Both tests passed!

;; Exercise 7.2
;; circles-position: number number number : K1 number number number : K2 ; Symbol
;; Explanation: Bestimmt die Lage der beiden Kreise zueinander
;; Example: 

(define (distance-centers x1 y1 x2 y2)
  (sqrt(+(sqr(- x1 x2 ))(sqr(- y1 y2))))
  )
(define (circles-position x1 y1 r1 x2 y2 r2)
  ;;Kreis1 ist im Kreis2 wenn R1 >= M1M2+R2
  ;;Kreis 1 schneidet Kreis 2 wenn M1M2 >=R1 / R2 und R1+R2 >= M1M2
  ;; Kreis1 ist in Kreis2 (oder umgekehrt) wenn die oberen Fälle nicht stimmen
  (cond [(>= r1(+(distance-centers x1 y1 x2 y2) r2))'Interior]
        [(and (>= (distance-centers x1 y1 x2 y2) r1)
              (>= (distance-centers x1 y1 x2 y2) r2)
              (>= (+ r1 r2) (distance-centers x1 y1 x2 y2)))'Intersect]
        [else 'External]
))

;; Tests


;; Exercise 7.3
;; calculate-properties: number number number symbol -> number
;; Explanation: Durch die Mermale eines Kreises können verschiedene Berechnungen 
;; Example: 
(define (calculate-properties x y r s)
 (if (>= r 0)   (
(cond [(symbol=? s 'Area) (* pi r r)]
      [(symbol=? s 'Circumference) (* 2 pi r)]
      [(symbol=? s 'Diameter) (* 2 r)]
      [(symbol=? s 'Distance) (sqrt(+(sqr(- x 0 ))(sqr(- y 0))))]
      [else 'Unknown_Property])
 (else 'r_is_negative)
 )))
;; Tests
