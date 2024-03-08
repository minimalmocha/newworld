import type { Metadata } from "next";
import "@/app/assets/scss/style.scss";
import Header from "@/app/components/header/page";
import Providers from "@/app/components/sessionProvider/Provider";
export const metadata: Metadata = {
  title: "newWorld",
  description: "Generated by create next app",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>
        <Providers>
          <Header />
          {children}
        </Providers>
      </body>
    </html>
  );
}
