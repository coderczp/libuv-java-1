package com.oracle.libuv;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class LibUVConfiguration {

    private Boolean useDirectByteBuffer;

    private Boolean copyBuffer;

    public static class Builder {

        private Boolean useDirectByteBuffer;

        private Boolean copyBuffer;

        public Builder useDirectByteBuffer(Boolean useDirectByteBuffer) {
            this.useDirectByteBuffer = useDirectByteBuffer;
            return this;
        }

        public Builder copyBuffer(Boolean copyBuffer) {
            this.copyBuffer = copyBuffer;
            return this;
        }

        public LibUVConfiguration build() {
            LibUVConfiguration configuration = new LibUVConfiguration();
            if (copyBuffer == null) {
            	copyBuffer = TRUE;
            }
            if (useDirectByteBuffer == null) {
            	useDirectByteBuffer = FALSE;
            }
            configuration.useDirectByteBuffer = useDirectByteBuffer;
            configuration.copyBuffer = copyBuffer;
            return configuration;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Boolean useDirectByteBuffer() {
        return useDirectByteBuffer;
    }

    public Boolean copyBuffer() {
        return copyBuffer;
    }

    @Override
    public String toString() {
        return "LibUVConfiguration [useDirectByteBuffer=" + useDirectByteBuffer + ", copyBuffer=" + copyBuffer + "]";
    }
}
