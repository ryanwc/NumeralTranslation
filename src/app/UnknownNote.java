package app;

/**
 * Store metadata from a parsed note (single line of notes) of unknown type.
 * 
 * Stores all imagined relevant data from initial parsing for later
 * analysis and classification.
 * 
 * Non-existant data is represented as '-1'.
 * For example, if there is no 'is', isPos = -1.
 * 
 * @author ryanwilliamconnor
 */
public class UnknownNote extends ParsedNote {
	
	private int countIs, countIntergalClust, countRomanBase, 
		countRomanComp, countComm, countQ,
		countArabic, countCredits, countHow, countMuch, countMany, isPos, 
		start1IntergalClust, end1IntergalClust, start2IntergalClust, 
		end2IntergalClust, romanPos, comm1Pos, comm2Pos, qPos, arabicPos, 
		creditPos, howPos, muchPos, manyPos;
	
	public UnknownNote(String note, String[] components, int countIs, 
			int countIntergalClust, int countRomanBase,
			int countRomanComp, int countComm, 
			int countQ, int countArabic, int countCredits, int countHow, 
			int countMuch, int countMany, int isPos, 
			int start1IntergalClust, int end1IntergalClust, 
			int start2IntergalClust, int end2IntergalClust, int romanPos, 
			int comm1Pos, int comm2Pos, int qPos, int arabicPos, 
			int creditPos, int howPos, int muchPos, int manyPos) {
		
		super(note, components);
		this.countIs = countIs;
		this.countIntergalClust = countIntergalClust;
		this.countRomanBase = countRomanBase;
		this.countRomanComp = countRomanComp;
		this.countComm = countComm;
		this.countQ = countQ;
		this.countArabic = countArabic;
		this.countCredits = countCredits;
		this.countHow = countHow;
		this.countMuch = countMuch;
		this.countMany = countMany;
		this.isPos = isPos;
		this.start1IntergalClust = start1IntergalClust;
		this.end1IntergalClust = end1IntergalClust;
		this.start2IntergalClust = start2IntergalClust;
		this.end2IntergalClust = end2IntergalClust;
		this.romanPos = romanPos;
		this.comm1Pos = comm1Pos;
		this.comm2Pos = comm2Pos;
		this.qPos = qPos;
		this.arabicPos = arabicPos;
		this.creditPos = creditPos;
		this.howPos = howPos;
		this.muchPos = muchPos;
		this.manyPos = manyPos;
	}
	
	@Override
	public String toString() {
		
		String str = "";
		str += super.getNote();
		str += '\n';
		str += "Components: ";
		for (int i = 0; i < super.getComponents().length; i++) {
			str += "'" + super.getComponents()[i] + "', ";
		}
		str += '\n';
		str += "Cis: " + countIs + ", CIntCl: " + countIntergalClust + 
				", CRomB: " + countRomanBase + ", CRomC: " + countRomanComp 
				+ ", CCm: " + countComm + ", CQ: " + countQ + ", CA: " + 
				countArabic + ", CCr: " + countCredits + ", CH: " + 
				countHow + ", CMu: " + countMuch + ", CM: " + countMany;
		str += '\n';
		str += "isP: " + isPos + ", " + "sIC1: " + start1IntergalClust 
				+ ", eIC1: " + end1IntergalClust + ", sIC2: " +
				+ start2IntergalClust + ", eIC2: " + end2IntergalClust + 
				", RP: " + romanPos + ", C1P: " + comm1Pos + 
				", C2P: " + comm2Pos + ", QP: " + qPos + 
				", AP: " + arabicPos + ", credP: " + creditPos + 
				", HP: " + howPos + ", muchP: " + muchPos + 
				", manyP: " + manyPos;
		str += '\n';
			
		return str;
	}

	public int getCountIs() {
		return countIs;
	}

	public void setCountIs(int countIs) {
		this.countIs = countIs;
	}

	public int getCountIntergalClust() {
		return countIntergalClust;
	}

	public void setCountIntergalClust(int countIntergalClust) {
		this.countIntergalClust = countIntergalClust;
	}

	public int getCountRomanBase() {
		return countRomanBase;
	}

	public void setCountRomanBase(int countRomanBase) {
		this.countRomanBase = countRomanBase;
	}

	public int getCountRomanComp() {
		return countRomanComp;
	}

	public void setCountRomanComp(int countRomanComp) {
		this.countRomanComp = countRomanComp;
	}

	public int getCountComm() {
		return countComm;
	}

	public void setCountComm(int countComm) {
		this.countComm = countComm;
	}

	public int getCountQ() {
		return countQ;
	}

	public void setCountQ(int countQ) {
		this.countQ = countQ;
	}

	public int getCountArabic() {
		return countArabic;
	}

	public void setCountArabic(int countArabic) {
		this.countArabic = countArabic;
	}

	public int getCountCredits() {
		return countCredits;
	}

	public void setCountCredits(int countCredits) {
		this.countCredits = countCredits;
	}

	public int getCountHow() {
		return countHow;
	}

	public void setCountHow(int countHow) {
		this.countHow = countHow;
	}

	public int getCountMuch() {
		return countMuch;
	}

	public void setCountMuch(int countMuch) {
		this.countMuch = countMuch;
	}

	public int getCountMany() {
		return countMany;
	}

	public void setCountMany(int countMany) {
		this.countMany = countMany;
	}

	public int getIsPos() {
		return isPos;
	}

	public void setIsPos(int isPos) {
		this.isPos = isPos;
	}

	public int getStart1IntergalClust() {
		return start1IntergalClust;
	}

	public void setStart1IntergalClust(int start1IntergalClust) {
		this.start1IntergalClust = start1IntergalClust;
	}

	public int getEnd1IntergalClust() {
		return end1IntergalClust;
	}

	public void setEnd1IntergalClust(int end1IntergalClust) {
		this.end1IntergalClust = end1IntergalClust;
	}

	public int getStart2IntergalClust() {
		return start2IntergalClust;
	}

	public void setStart2IntergalClust(int start2IntergalClust) {
		this.start2IntergalClust = start2IntergalClust;
	}

	public int getEnd2IntergalClust() {
		return end2IntergalClust;
	}

	public void setEnd2IntergalClust(int end2IntergalClust) {
		this.end2IntergalClust = end2IntergalClust;
	}

	public int getRomanPos() {
		return romanPos;
	}

	public void setRomanPos(int romanPos) {
		this.romanPos = romanPos;
	}

	public int getComm1Pos() {
		return comm1Pos;
	}

	public void setComm1Pos(int comm1Pos) {
		this.comm1Pos = comm1Pos;
	}

	public int getComm2Pos() {
		return comm2Pos;
	}

	public void setComm2Pos(int comm2Pos) {
		this.comm2Pos = comm2Pos;
	}

	public int getqPos() {
		return qPos;
	}

	public void setqPos(int qPos) {
		this.qPos = qPos;
	}

	public int getArabicPos() {
		return arabicPos;
	}

	public void setArabicPos(int arabicPos) {
		this.arabicPos = arabicPos;
	}

	public int getCreditPos() {
		return creditPos;
	}

	public void setCreditPos(int creditPos) {
		this.creditPos = creditPos;
	}

	public int getHowPos() {
		return howPos;
	}

	public void setHowPos(int howPos) {
		this.howPos = howPos;
	}

	public int getMuchPos() {
		return muchPos;
	}

	public void setMuchPos(int muchPos) {
		this.muchPos = muchPos;
	}

	public int getManyPos() {
		return manyPos;
	}

	public void setManyPos(int manyPos) {
		this.manyPos = manyPos;
	}	
}
