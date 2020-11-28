package com.rentpal.accounts.constants;

/**
 * The Class Constants.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jun 22, 2020 4:59:59 PM
 * This class holds the constant fields
 */
public class Constants {

	/** The Constant SUCCESS. */
	public static final String SUCCESS="success";
	
	/** The Constant FAILED. */
	public static final String FAILED="failed ";
	
	/** The Constant ALPHANUMLEN. */
	public static final int ALPHANUMLEN=16;
	
	/** The Constant EXPIRATIONINTERVAL. */
	public static final long EXPIRATIONINTERVAL=86400000;

	/**
	 * The Enum Tokentype.
	 */
	public enum Tokentype {
		
		/** The verify. */
		VERIFY(1),
		
		/** The reset. */
		RESET(2);
		
		/** The value. */
		private final int value;
		
		/**
		 * Instantiates a new tokentype.
		 *
		 * @param val the val
		 */
		private Tokentype(int val) {
			this.value=val;
		}
		
		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public int getValue() {
	        return value;
	    }
	}
}
