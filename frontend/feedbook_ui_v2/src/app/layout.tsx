import type {Metadata} from "next";
import {Geist, Geist_Mono} from "next/font/google";
import "./globals.css";
import AppBar from "@/components/AppBar";
import QueryProvider from "@/containers/QueryProvider";
import MswProvider from "@/containers/MswProvider";

const geistSans = Geist({
    variable: "--font-geist-sans",
    subsets: ["latin"],
});

const geistMono = Geist_Mono({
    variable: "--font-geist-mono",
    subsets: ["latin"],
});

export const metadata: Metadata = {
    title: "Feedbook",
    description: "Simple SNS Project",
    icons: '/images/icon.png'
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
        <MswProvider>
            <QueryProvider>
                <main className="min-h-screen bg-gray-50">
                    <div className="relative flex min-h-screen flex-col">
                        <AppBar>
                            {children}
                        </AppBar>
                    </div>
                </main>
            </QueryProvider>
        </MswProvider>
        </body>
        </html>
    );
}
