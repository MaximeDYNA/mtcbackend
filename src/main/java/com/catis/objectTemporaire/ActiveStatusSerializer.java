package com.catis.objectTemporaire;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class ActiveStatusSerializer extends StdSerializer<Boolean> {

    public ActiveStatusSerializer() {
        this(null);
    }

    protected ActiveStatusSerializer(Class<Boolean> t) {
        super(t);
    }

    @Override
    public void serialize(Boolean value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value != null) {
            gen.writeString(value ? "Activé" : "Désactivé");
        } else {
            gen.writeNull();
        }
    }
}