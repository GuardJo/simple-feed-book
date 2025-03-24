import type {Metadata} from "next";
import {Geist, Geist_Mono} from "next/font/google";
import "./globals.css";
import AppBar from "@/components/AppBar";

const geistSans = Geist({
    variable: "--font-geist-sans",
    subsets: ["latin"],
});

const geistMono = Geist_Mono({
    variable: "--font-geist-mono",
    subsets: ["latin"],
});

export const metadata: Metadata = {
    title: "Simple Feed Book",
    description: "Simple SNS Project",
};

export default function RootLayout({
                                       children,
                                   }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <html lang="ko">
        <body
            className={`${geistSans.variable} ${geistMono.variable} antialiased`}
        >
        <main className="min-h-screen bg-gray-50">
            <div className="relative flex min-h-screen flex-col">
                <AppBar>
                    {children}
                </AppBar>
            </div>
        </main>
        </body>
        </html>
    );
}
