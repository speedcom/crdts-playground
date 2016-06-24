Paper https://hal.inria.fr/inria-00177693/document

CRDTs jest strukturą danych, która została zaprojektowana z myślą o ewentualnej spójności danych różnych komponentów 
(np. znajdujących się na różnych maszynach) bez potrzeby ich jawnej synchronizacji.

Model ewentualnej spójności danych zapewnia, że z czasem wszystkie dane różnych komponentów zostaną uwspólnione, tj. będą takie same. 
Nie oznacza to jednak, że przy odczytach w pewnym punkcie w czasie, każdy klient dostanie ten sam zbiór danych (mogą się one różnić).

Istnieją dwa rodzaje CRDT:
- operation-based
- state-based

State-based
W tym modelu kiedy CRDT otrzyma do wykonania operacje od klienta, zmienia swoja wartosc odpowiednio i rozsyla wiadomosci do reszty CRDTsow
w klastrze. Te wiadomosci, ktore sa wysylane zawieraja caly stan CRDTs-a. Kiedy inny CRDT otrzyma te wiadomosc aktualizujacą, nastepuje
wykonanie operacji łączącej (ang. merge). Ta ostatnia operacja musi zapewnic, że jej wykonanie spowoduje, że ostateczny wspólny stan zostanie
osiągnięty. Operacja ta musi miec nastepujace cechy:
- associative
- commutative
- idempotent




