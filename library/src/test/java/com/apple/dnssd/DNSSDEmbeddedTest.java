package com.apple.dnssd;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@Ignore
@PrepareForTest({DNSSDEmbedded.class})
public class DNSSDEmbeddedTest {

    private static final int TIMEOUT = 5000;

    DNSSDEmbedded mDNSSDEmbedded;

    @Before
    public void setup() {
        mDNSSDEmbedded = new DNSSDEmbedded(TIMEOUT);
        mockStatic(DNSSDEmbedded.class);
    }

    @Test
    public void init() throws InterruptedException {
        mDNSSDEmbedded.init();
        PowerMockito.verifyStatic();
        DNSSDEmbedded.Init();
        PowerMockito.verifyStatic(timeout(TIMEOUT));
        DNSSDEmbedded.Loop();
    }

    @Test
    public void initWithError() throws InterruptedException {
        PowerMockito.when(DNSSDEmbedded.Init()).thenReturn(-1);
        mDNSSDEmbedded.init();
        PowerMockito.verifyStatic();
        DNSSDEmbedded.Init();
        PowerMockito.verifyStatic(after(TIMEOUT).never());
        DNSSDEmbedded.Loop();
    }

    @Test
    public void exit() throws InterruptedException {
        mDNSSDEmbedded.exit();
        PowerMockito.verifyStatic(timeout(2 * TIMEOUT));
        DNSSDEmbedded.Exit();
    }

    @Test
    public void cancelStopTimer() throws InterruptedException {
        mDNSSDEmbedded.init();
        PowerMockito.verifyStatic();
        DNSSDEmbedded.Init();
        PowerMockito.verifyStatic(timeout(TIMEOUT));
        DNSSDEmbedded.Loop();
        mDNSSDEmbedded.exit();
        Thread.sleep(TIMEOUT / 2);
        mDNSSDEmbedded.init();
        Thread.sleep(TIMEOUT / 2);
        PowerMockito.verifyStatic(never());
        DNSSDEmbedded.Exit();
        mDNSSDEmbedded.exit();
        PowerMockito.verifyStatic(timeout(2 * TIMEOUT));
        DNSSDEmbedded.Exit();
    }

    @After
    public void tearDown() {
    }
}
