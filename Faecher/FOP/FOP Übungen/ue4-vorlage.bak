;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname ue4-vorlage) (read-case-sensitive #t) (teachpacks ((lib "image.rkt" "teachpack" "2htdp"))) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ((lib "image.rkt" "teachpack" "2htdp")) #f)))
;; The alphabet we use is encoded in eight bits and the numbers correspond to ASCII symbols.
;; ASCII number 27 "\e" is used as termination symbol.


(define (convert-to-base-four num)
  
  )

(check-expect (convert-to-base-four 10) (list 2 2))
(check-expect (convert-to-base-four 100) (list 1 2 1 0))


(define (string->encodeable s)
    
  )

(check-expect (string->encodeable "fop") (list (list 1 2 1 2) (list 1 2 3 3) (list 1 3 0 0)))
(check-expect (string->encodeable "FOP") (list (list 1 0 1 2) (list 1 0 3 3) (list 1 1 0 0)))

(define (encodeable->string lon)
    
  )

(check-expect (encodeable->string (list (list 1 2 1 2) (list 1 2 3 3) (list 1 3 0 0))) "fop")
(check-expect (encodeable->string (list (list 1 0 1 2) (list 1 0 3 3) (list 1 1 0 0))) "FOP")

;; load-image: string -> (list of color)
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


(define (steganographie-enc loc m k)
  
  )


(define (steganographie-dec loc k)
    
  )
