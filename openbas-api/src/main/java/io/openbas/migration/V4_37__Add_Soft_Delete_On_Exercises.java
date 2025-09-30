package io.openbas.migration;

import java.sql.Statement;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.stereotype.Component;

@Component
public class V4_37__Add_Soft_Delete_On_Exercises extends BaseJavaMigration {
  @Override
  public void migrate(Context context) throws Exception {
    try (Statement statement = context.getConnection().createStatement()) {
      // Add a column for soft delete
      statement.executeUpdate(
          "ALTER TABLE exercises ADD COLUMN exercise_deleted_at TIMESTAMP WITH TIME ZONE DEFAULT NULL;");
    }
  }
}
