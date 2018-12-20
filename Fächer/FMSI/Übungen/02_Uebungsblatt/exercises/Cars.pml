/*4 Autos auf der Kreuzung. Verifizierung: niemals zwei Autos gleichzeitigauf Kreuzung */
bool NS
bool SN
bool OW
bool WO
byte i = 0
active proctype Kreuzung(){
do
	::if
		:: SN = true;
		:: OW = true;
		:: NS = true;
		:: WO = true;
	fi
	::if
		::OW == false -> SN = true;
		::NS == false -> OW = true;
		::WO == false-> NS = true;
		::SN == false -> WO = true;
	fi

	::i = i +1;
	::if
		:: i >= 2 -> break;
		
	
}


