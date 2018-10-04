public class ApartmentRental implements Payment extends RentalProperty
{
	public double amount()
	{
		return origRent*1.04;
	}
}
