package com.oracle.libuv;

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

    public void setCopyBuffer(Boolean copyBuffer) {
        this.copyBuffer = copyBuffer;
    }

    @Override
    public String toString() {
        return "LibUVConfiguration [useDirectByteBuffer=" + useDirectByteBuffer + ", copyBuffer=" + copyBuffer + "]";
    }
}
