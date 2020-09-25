# Accenture Java Competition / REBOARDING 2.0
## 2. FELADAT

A létszám alapú foglalási rend nem felel meg a globális, távolság alapú elkülönítési szabályoknak, ezért a
meglévő alkalmazást módosítani szükséges:

A mellékelt iroda alaprajzon halványzöld háttérszínnel jelöltük azon munkahelyeket, melyek foglalhatóak a
rendszerben. 1 képpontot 10x10 cm-es területnek számolva, ez az alaprajz alkalmas az egyes
munkahelyek közötti távolság megállapítására.<br>
A rendszernek a módosítás után a konfigurációban megadott minimális távolság figyelembe vételével kell
kiosztania a munkahelyeket. Az induló érték 5 méter (50 képpont), melyet az újranépesítés egyes
lépcsőiben 4, 3, 2 majd 1 méterre kíván a HR csökkenteni.<br>
Ennek megfelelően most nem elegendő a bent tartózkodók létszámával számolni, hanem konkrét
munkahelyet kell (a foglaláskor vagy legkésőbb a belépéskor) kiosztani, melynek a helyét egy erre a célra
kialakítandó HTTP GET végponton vizuálisan is meg kell tudni jeleníteni. A foglaláskor a rendszer adja
meg azt az URL-t, amin elérhető a kép, melyen a kiosztott munkahely egyértelműen látható. Ezt a képet
az applikáció fogja letölteni és a felhasználó részére megjeleníteni.

A megoldás részeként javasoljuk a megadott iroda alaprajz direktben történő felhasználását, esetleg
egyszerű transzformáció alkalmazásával módosítva (pl.: székek egyértelműen eltérő színnel történő
jelzése), hiszen az is elképzelhető, hogy a HR az iroda egyes részeit lezárja, vagy megnyitja a még lezárt
(jelenleg még más háttérszínnel jelölt) részeket, és ezt a rendszer konfiguráció módosításával kell tudni
támogatni.

A HR szeretné, ha folyamatosan követhető lenne vizuális formában is az iroda foglalása, ezért egy további
endpoint megvalósítása is szükséges, melyen HTTP GET kérés formájában a /layout URL-en képi
formában letölthető az aktuális alaprajz, rajta eltérő színnel jelölve azon munkahelyek, amelyek foglaltak
(piros színnel), azok, amelyekre van foglalás, de a munkatárs még nem érkezett be (sárga színnel),
valamint a szabad munkahelyek, amelyek még kioszthatóak az aktuális védőtávolság betartása mellett
(zöld színnel).

A rendszernek ezen felül támogatni kell a VIP vendégek és munkatársak kezelését, akik számára nem
szükséges helyet foglalni, és ők minden más korlátozás ellenére is beléphetnek, ezzel akár a létszám, akár
a távolság alapú szabályokat is felülbírálva. A VIP személyek listáját a konfiguráció kell tartalmazza.

A felhasználók kényelmét szolgálva a rendszernek nemcsak a várólista pozíció megadásával kell
támogatni az irodába történő indulási időpont meghatározását, hanem egy Kafka topicba történő esemény
küldéssel is értesíteni kell az applikációt (ami a foglaláskor történő várólistára kerülés esetén automatikusan
feliratkozik erre a csatornára), amikor a felhasználó a várólistában eléri a 3. (preferáltan konfigurálható)
pozíciót.

A vezető architektúra szakértő kifogásolta a korábbi megvalósítást, hogy nem elegendően felel meg a
funkció orientált architektúrának. Ezen igény figyelembe vételével a fenti módosításokkal együtt az
architektúra átszervezése is végrehajtható.

**Feladat leadás:**<br>
**A megoldások beadása GitHub/GitLab repository megosztásával a JunctionApp felületén történik.**<br>
**Leadási határidő: 06.29 23.59**

**[BACK TO README](README.md)**<br>
