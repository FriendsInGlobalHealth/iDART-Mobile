package mz.org.fgh.idartlite.model;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import mz.org.fgh.idartlite.base.BaseModel;
import mz.org.fgh.idartlite.dao.PharmacyTypeDaoImpl;

import java.util.Objects;

@DatabaseTable(tableName = "pharmacy_type", daoClass = PharmacyTypeDaoImpl.class)
public class PharmacyType extends BaseModel {


	public static final String COLUMN_ID = "id";
	public static final String COLUMN_DESCRIPTION = "description";

	@DatabaseField(columnName = COLUMN_ID, id = true)
	private int id;

	@DatabaseField(columnName = COLUMN_DESCRIPTION)
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PharmacyType that = (PharmacyType) o;
		return description.equals(that.description);
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@Override
	public int hashCode() {
		return Objects.hash(description);
	}

	@Override
	public String toString() {
		return "PharmacyType{" +
				"description='" + description + '\'' +
				'}';
	}
}
