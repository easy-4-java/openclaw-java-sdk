package io.github.easy4j.openclaw.ws;

/**
 * Gateway 设备身份签名器。
 * <p>
 * SDK 会在收到 {@code connect.challenge} 后，根据当前 nonce、连接角色、权限范围和认证令牌
 * 构造 OpenClaw v3 设备认证载荷，再调用本接口完成签名。实现方应使用与
 * {@link #getPublicKey()} 对应的 Ed25519 私钥签名，并返回 Base64 URL 编码且不带填充的签名字节。
 * 私钥不应交给 SDK 或写入日志。
 * </p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public interface OpenClawGatewayDeviceIdentity {

    /**
     * 返回由设备公钥原始字节计算得到的稳定设备标识。
     *
     * @return 设备标识，通常为公钥原始字节的 SHA-256 十六进制摘要
     */
    String getDeviceId();

    /**
     * 返回 Gateway 可识别的 Ed25519 公钥。
     *
     * @return Base64 URL 编码且不带填充的 Ed25519 原始公钥
     */
    String getPublicKey();

    /**
     * 使用设备私钥签署本次 challenge 的 UTF-8 载荷。
     *
     * @param payload SDK 按 OpenClaw v3 设备认证协议生成的完整签名载荷
     * @return Base64 URL 编码且不带填充的 Ed25519 签名
     */
    String sign(String payload);
}
