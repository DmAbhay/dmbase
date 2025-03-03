package dataman.dmbase.server;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import dataman.dmbase.debug.Debug;
import dataman.dmbase.dto.RecId;

@Service
public class DmBaseServer {

	public String vNull(Object value, String defaultValue) {
		int length = defaultValue.length(); // Get the length of the default value

		if (value == null) {
			return defaultValue; // Return default if value is null
		}

		try {
			int number = Integer.parseInt(value.toString()); // Convert to integer
			return String.format("%0" + length + "d", number); // Format with leading zeros
		} catch (NumberFormatException e) {
			return defaultValue; // Return default if conversion fails
		}
	}

	public static Double vNull(Object value) {
		if (value == null || value.toString().trim().isEmpty()) {
			return 0.0; // Return default value of 0.0
		}

		try {
			return Double.parseDouble(value.toString()); // Convert to Double safely
		} catch (NumberFormatException e) {
			return 0.0; // Return 0.0 if parsing fails
		}
	}

	public static BigInteger iNull(Object value) {
		if (value == null) {
			value = 0;
		}
		if (String.valueOf(value).equalsIgnoreCase("")) {
			value = 0;
		}
		if (value.toString().contains(".")) {
			try {
				value = value.toString().split("\\.")[0];
			} catch (Exception e) {
				value = 0;
			}
			if (String.valueOf(value).equalsIgnoreCase("")) {
				value = 0;
			}
		}
		return new BigInteger(value.toString());
	}

	public String getSerialNoInteger(String siteCode, String tableName, JdbcTemplate jdbcTemplate) {
		String sql = "EXEC getCode ?, ?, 0"; // Correct way to use parameters

		try {
			System.out
					.println("Executing Query: " + sql + " with tableName=" + tableName + " and siteCode=" + siteCode);

			Integer result = jdbcTemplate.queryForObject(sql, Integer.class, tableName, siteCode);
			System.out.println("Query Result: " + result);

			return "1" + vNull(siteCode, "0000") + vNull(result, "0000");
		} catch (Exception e) {
			throw new RuntimeException("Error fetching serial number: " + e.getMessage(), e);
		}
	}

//    public String getSerialNoBigInt(String siteCode, String tableName, JdbcTemplate jdbcTemplate) {
//        String sql = "EXEC getCode ?, ?, 0";
//
//        try {
//            Long result = jdbcTemplate.queryForObject(sql, new Object[]{tableName, siteCode}, Long.class);
//            return "1" + vNull(siteCode, "0000") + vNull(result, "00000000000000");
//        } catch (Exception e) {
//            throw new RuntimeException("Error fetching serial number: " + e.getMessage(), e);
//        }
//    }
//
//    public String getSerialNoBigInt(String siteCode, String vPrefix, String tableName, JdbcTemplate jdbcTemplate) {
//        if (iNull(siteCode).longValue() == 0) {
//            throw new UnsupportedOperationException("Sitecode is mandatory.");
//        }
//
//        if (iNull(vPrefix).longValue() == 0) {
//            throw new UnsupportedOperationException("Voucher Prefix is mandatory.");
//        }
//
//        String sql = "EXEC getCode ?, ?, ?";
//
//        try {
//            Integer result = jdbcTemplate.queryForObject(sql, new Object[]{tableName, siteCode, vPrefix}, Integer.class);
//
//            return "1" + vNull(siteCode, "0000") + vNull(vPrefix, "00") + vNull(result, "00000000000");
//        } catch (Exception e) {
//            throw new UnsupportedOperationException(e.getMessage(), e);
//        }
//    }
//
//
//
//    public String getDocId(String voucherType, String voucherPrefix, String siteCode, JdbcTemplate jdbcTemplate) {
//        String sql = "EXEC getDocId ?, ?, ?";
//
//        String voucherNo = jdbcTemplate.query(sql, new Object[]{voucherType, voucherPrefix, siteCode}, rs -> {
//            if (rs.next()) {
//                return String.valueOf(vNull(rs.getString(1)).intValue());
//            }
//            return ""; // Return empty string if no result is found
//        });
//
//        if (voucherNo.isEmpty() || Double.parseDouble(voucherNo) <= 0) {
//            throw new UnsupportedOperationException("Unable to generate docId.");
//        }
//
//        //String docId = "1" + vNull(vNull(siteCode), "0000") + vNull(vNull(voucherType), "000") + vNull(vNull(voucherPrefix), "00") + vNull(vNull(voucherNo), "00000000");
//
//        //return docId;
//        return "1" +
//                vNull(siteCode, "0000") +
//                vNull(voucherType, "000") +
//                vNull(voucherPrefix, "00") +
//                vNull(voucherNo, "00000000");
//    }

	public String getSerialNoBigInt(String siteCode, String tableName, JdbcTemplate jdbcTemplate) {
		String sql = "EXEC getCode ?, ?, 0";

		try {
			Long result = jdbcTemplate.queryForObject(sql, Long.class, tableName, siteCode);
			return "1" + vNull(siteCode, "0000") + vNull(result, "00000000000000");
		} catch (Exception e) {
			throw new RuntimeException("Error fetching serial number: " + e.getMessage(), e);
		}
	}

	public String getSerialNoBigInt(String siteCode, String vPrefix, String tableName, JdbcTemplate jdbcTemplate) {
		if (iNull(siteCode).longValue() == 0) {
			throw new UnsupportedOperationException("Sitecode is mandatory.");
		}

		if (iNull(vPrefix).longValue() == 0) {
			throw new UnsupportedOperationException("Voucher Prefix is mandatory.");
		}

		String sql = "EXEC getCode ?, ?, ?";

		try {
			Integer result = jdbcTemplate.queryForObject(sql, Integer.class, tableName, siteCode, vPrefix);
			return "1" + vNull(siteCode, "0000") + vNull(vPrefix, "00") + vNull(result, "00000000000");
		} catch (Exception e) {
			throw new UnsupportedOperationException(e.getMessage(), e);
		}
	}

	public String getDocId(String voucherType, String voucherPrefix, String siteCode, JdbcTemplate jdbcTemplate) {
		String sql = "EXEC getDocId ?, ?, ?";

		String voucherNo = jdbcTemplate.query(sql, (PreparedStatementSetter) ps -> {
			ps.setString(1, voucherType);
			ps.setString(2, voucherPrefix);
			ps.setString(3, siteCode);
		}, (ResultSetExtractor<String>) rs -> {
			if (rs.next()) {
				return String.valueOf(vNull(rs.getString(1)).intValue());
			}
			return ""; // Return empty string if no result is found
		});

		if (voucherNo.isEmpty() || Double.parseDouble(voucherNo) <= 0) {
			throw new UnsupportedOperationException("Unable to generate docId.");
		}

		return "1" + vNull(siteCode, "0000") + vNull(voucherType, "000") + vNull(voucherPrefix, "00")
				+ vNull(voucherNo, "00000000");
	}

	public RecId getRecId(String tableName, String keyFieldName, String keyValue, String recIdFieldName, RecId recId,
			String dateValue, String voucherTypeFieldName, String voucherType, String voucherPrefix, String siteCode,
			String siteShortName, String companyCode, Boolean isCheckDate, HashMap<String, String> paramFormat,
			JdbcTemplate jdbcTemplate) throws Exception {

		if (recId == null) {
			throw new IllegalArgumentException("Please mention recId.");
		}

// Initialize recId value
		recId.setRecIdValue(recId.getRecIdValue() != null ? recId.getRecIdValue() : "");

// Validate inputs
		if (voucherType == null || voucherType.trim().isEmpty()) {
			throw new IllegalArgumentException("Please mention voucher type.");
		}
		if (dateValue == null || dateValue.trim().isEmpty()) {
			throw new IllegalArgumentException("Please mention date.");
		}

		String v_SN = fetchVoucherType(voucherType, jdbcTemplate);

		System.out.println(v_SN);

// Initialize variables for the number system
		String nsIncludeV_Type = "", nsFormat = "", nsCounterFormat = "0", companySName = "", voucherSName = v_SN;
		boolean isYearWise = true, isSiteWise = true;

// SQL query to get number system format
		String sqlQuery = "SELECT TOP 1 nsf.nsFormat, nsf.counterFormat, nsf.isYearWise, nsf.isSiteWise, ns.includeV_Type "
				+ "FROM numberSystem ns JOIN numberSystemFormat nsf ON ns.nsGroup = nsf.nsGroup "
				+ "WHERE ns.v_Type = ? AND ISNULL(nsf.site_Code, ?) = ? " + "ORDER BY nsf.site_Code DESC;";

		try {
// Get number system format
			Map<String, Object> nsResult = jdbcTemplate.queryForMap(sqlQuery, voucherType, siteCode, siteCode);

// Extract results
			nsIncludeV_Type = (String) nsResult.get("includeV_Type");
			nsFormat = (String) nsResult.get("nsFormat");
			nsCounterFormat = (String) nsResult.get("counterFormat");

//voucherSName = (String) nsResult.get("short_name");

// Retrieve as Short and convert to boolean
			Short isYearWiseShort = (Short) nsResult.get("isYearWise");
			isYearWise = (isYearWiseShort != null && isYearWiseShort != 0);

			Short isSiteWiseShort = (Short) nsResult.get("isSiteWise");
			isSiteWise = (isSiteWiseShort != null && isSiteWiseShort != 0);
		} catch (EmptyResultDataAccessException e) {
			throw new IllegalArgumentException(
					"No number system format found for the given voucher type and site code.");
		}

		if (nsFormat.trim().isEmpty()) {
			throw new IllegalArgumentException("Please define number format.");
		}
		if (nsCounterFormat.trim().isEmpty()) {
			throw new IllegalArgumentException("Please define counter format.");
		}

		if (dateValue == null || dateValue.trim().isEmpty()) {
			throw new IllegalArgumentException("Please mention date.");
		}

// Parse dateValue to java.sql.Date
		java.sql.Date sqlDate;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); // Only MM/dd/yyyy format
			java.util.Date parsedDate = formatter.parse(dateValue);
			sqlDate = new java.sql.Date(parsedDate.getTime());
			System.out.println("SQL Date: " + sqlDate.toString());
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date format. Expected format: MM/dd/yyyy", e);
		}

// Set the date components
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sqlDate);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1; // Calendar months are 0-based, so we add 1
		int yearFull = calendar.get(Calendar.YEAR);
		int year = yearFull % 100; // Last two digits of the year

		if (recId.getRecIdValue().trim().isEmpty()) {
			nsFormat = nsFormat.replace("{sitesname}", siteShortName)
					.replace("{site}", String.format("%03d", Integer.parseInt(siteCode)))
					.replace("{voucherprefix}", String.format("%02d", Integer.parseInt(voucherPrefix)))
					.replace("{company}", companySName).replace("{year}", String.format("%02d", year))
					.replace("{yearfull}", String.format("%04d", yearFull))
					.replace("{month}", String.format("%02d", month)).replace("{day}", String.format("%02d", day))
					.replace("{voucher}", String.format("%03d", Integer.parseInt(voucherType)))
					.replace("{vouchersname}", voucherSName);

			if (paramFormat != null) {
				for (String keySet : paramFormat.keySet()) {
					nsFormat = nsFormat.replace("{" + keySet + "}", paramFormat.get(keySet).trim());
				}
			}

			recId.setPrefix(nsFormat.replace("{counter}", ""));

			if (recId.getCounter() == null) {
				// Initialize the counter to 0 if it's null
				recId.setCounter(0L); // Ensure that it's Long
			}

// Increment or retrieve counter
			if (recId.getCounter() == 0) {
				String counterSql = "EXEC getRecId ?, ?, ?, ?";
				// Assuming the counter is returned as Long
				Long counterValue = jdbcTemplate.queryForObject(counterSql, Long.class, voucherType,
						isYearWise ? voucherPrefix : null, isSiteWise ? siteCode : null, recId.getPrefix());

				// Check if counterValue is null and handle accordingly
				if (counterValue != null) {
					recId.setCounter(counterValue);
				} else {
					// Handle the case where counterValue is null (e.g., log or throw an exception)
					throw new IllegalStateException("Counter value returned by stored procedure is null.");
				}
			} else {
				// Check for existing counters
				String checkCounterSql = "SELECT [counter] FROM numberSystemCounter WHERE nsGroup = ? AND "
						+ "ISNULL(v_Prefix, 0) = ISNULL(?, 0) AND ISNULL(site_Code, 0) = ISNULL(?, 0) AND "
						+ "ISNULL(nsFormatValue, '') = ISNULL(?, '') AND ISNULL([counter], 0) > ?";
				Short existingCount = jdbcTemplate.queryForObject(checkCounterSql, Short.class, voucherType,
						voucherPrefix, siteCode, recId.getPrefix(), recId.getCounter());

				if (existingCount != null && existingCount > 0) {
					throw new IllegalArgumentException("Given counter cannot be greater than existing counter.");
				}
			}

// Set final record ID value
			nsFormat = nsFormat.replace("{counter}",
					String.format("%0" + nsCounterFormat.length() + "d", recId.getCounter()));
			recId.setRecIdValue(nsFormat);
		}

// Check for duplicates
		if (!recId.getRecIdValue().trim().isEmpty()) {
			if (voucherTypeFieldName.trim().isEmpty()) {
				voucherTypeFieldName = "v_Type";
			}
			String duplicateCheckSql = "SELECT COUNT(" + recIdFieldName + ") AS Cnt FROM " + tableName + " WHERE "
					+ recIdFieldName + " = ? AND " + keyFieldName + " <> ? AND " + voucherTypeFieldName + " IN ("
					+ getParamList(nsIncludeV_Type) + ")";

			if (isYearWise) {
				duplicateCheckSql += " AND v_Prefix = ?";
			}
			if (isSiteWise) {
				duplicateCheckSql += " AND site_Code = ?";
			}

// Prepare parameters for duplicate check
			List<Object> params = new ArrayList<>();
			params.add(recId.getRecIdValue());
			params.add(keyValue);
			Collections.addAll(params, nsIncludeV_Type.split(","));
			if (isYearWise) {
				params.add(voucherPrefix);
			}
			if (isSiteWise) {
				params.add(siteCode);
			}

			int count = jdbcTemplate.queryForObject(duplicateCheckSql, Integer.class, params.toArray());
			if (count > 0) {
				throw new IllegalArgumentException("Duplicate voucher no.");
			}
		}

		if (recId.getRecIdValue().trim().isEmpty()) {
			throw new IllegalArgumentException("Voucher no. is mandatory.");
		}

		return recId;
	}

	private String getParamList(String nsIncludeV_Type) {
		String[] types = nsIncludeV_Type.split(",");
		StringBuilder paramList = new StringBuilder();
		for (int i = 0; i < types.length; i++) {
			if (i > 0) {
				paramList.append(",");
			}
			paramList.append("?");
		}
		return paramList.toString();
	}

	private String fetchVoucherType(String voucherType, JdbcTemplate jdbcTemplate) throws Exception {
		String sql = "SELECT TOP 1 vt.short_Name FROM voucher_Type vt WHERE vt.isActive = 1 AND vt.v_Type = ?";
		try {
// Use parameterized query to prevent SQL injection
			@SuppressWarnings("deprecation")
			String voucherName = jdbcTemplate.queryForObject(sql, new Object[] { voucherType }, String.class);
			return voucherName; // Return the result directly if found
		} catch (EmptyResultDataAccessException e) {
// Handle the case where no result is found
			throw new Exception("Voucher Type not found for type: " + voucherType, e);
		} catch (DataAccessException e) {
// Handle other database-related exceptions
			throw new Exception("Database error occurred while fetching voucher type: " + voucherType, e);
		}
	}

}
