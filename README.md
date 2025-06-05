# Ülesanded:
1. Edetabelit ei saa vaadata mängu ajal.
2. Mängulaua suurust ei saa muuta mängu ajal.
3. Mängu lõppedes ei kao lõppseis ekraanilt (pilt Gameboardil)
4. Kui muudetakse mängulaua suurust, siis eelmise mängu seis kustub (hetkel jookseb kokku) -pilt kaob.
5. Kogu mängu aknal võiks olla miinimum suurus, millest väiksemaks ei saa (suuremaks tohib, aga väiksemaks tõmmata mitte) (võiks olla seotud veergude arvuga - vaata seda Dialogbox metodit, kus määratakse suurust).
6. "eraldi aknas" ilma linnukeseta, tähendab, et edetabel tuleb luua olemasoleva paneeli peale. Tuleb tekitada eraldi paneel (kolmas paneel -floLayoutManager vms), läheb selle kollase ja sinise paneeli peale füüsiliselt. Lisaks vaja tekitada "sulge nupp" - tapad enda paneeli.