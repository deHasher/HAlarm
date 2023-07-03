package ru.dehasher.halarm.managers;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinUser;
import ru.dehasher.halarm.HAlarm;
import java.util.concurrent.CompletableFuture;

public class KeyHook {
    private static User32.HHOOK hhk;

    public static void block() {
        CompletableFuture.runAsync(() -> {
            WinDef.HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
            hhk = User32.INSTANCE.SetWindowsHookEx(User32.WH_KEYBOARD_LL, (User32.LowLevelKeyboardProc) (nCode, wParam, info) -> {
                if (nCode >= 0) {
                    switch (wParam.intValue()) {
                        case WinUser.WM_KEYUP:
                        case WinUser.WM_KEYDOWN:
                        case WinUser.WM_SYSKEYUP:
                        case WinUser.WM_SYSKEYDOWN:
                            if (!HAlarm.isSuccess()) {
                                if (info.vkCode == 17 || info.vkCode == 0xA2 || info.vkCode == 0xA3) Methods.reload();
                                if (!((info.vkCode >= 0x30 && info.vkCode <= 0x39) || (info.vkCode >= 0x01 && info.vkCode <= 0x06) || info.vkCode == 0x08)) return new LRESULT(1);
                            }
                            break;
                    }
                }
                Pointer ptr = info.getPointer();
                long peer   = Pointer.nativeValue(ptr);
                return User32.INSTANCE.CallNextHookEx(hhk, nCode, wParam, new WinDef.LPARAM(peer));
            }, hMod, 0);
            int result;
            User32.MSG msg = new User32.MSG();
            while ((result = User32.INSTANCE.GetMessage(msg, null, 0, 0)) != 0) {
                if (result == -1) {
                    break;
                } else {
                    User32.INSTANCE.TranslateMessage(msg);
                    User32.INSTANCE.DispatchMessage(msg);
                }
            }
            User32.INSTANCE.UnhookWindowsHookEx(hhk);
        });
    }
}