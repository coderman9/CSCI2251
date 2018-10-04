public class SingleFamilyRental implements Payment extends RentalProperty
{
	public double amount()
	{
		return origRent*1.08;
	}
}
