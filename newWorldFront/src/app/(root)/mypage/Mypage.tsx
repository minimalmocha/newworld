"use client";
import React from "react";
import styles from "@/app/assets/scss/section/_mypage.module.scss";
import dummy from "./dummy.json";
import dummy2 from "./dummy2.json";
import { useState, useEffect } from "react";
import WelcomeMessage from "@/app/components/mypageComponents/WelcomeMessage";
import PrivacyControlBox from "@/app/components/mypageComponents/PrivacyControlBox";
import SelectedItem from "@/app/components/mypageComponents/SelectedItem";
import { MypageProps, WelcomeMessageProps } from "@/app/types/mypage";
import usePagination from "@/app/hooks/UsePagination";
import { getUserClearQuizzes } from "@/app/lib/api/mypageapi";

type CombinedProps = MypageProps & WelcomeMessageProps;
const Mypage: React.FC<CombinedProps> = ({ session }) => {
  const [quizzes, setQuizzes] = useState([]);
  const [quizzesLength, setQuizzesLength] = useState([]);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [days, setDays] = useState(0);
  const [loading, setLoading] = useState(true);
  const [selectedItem, setSelectedItem] = useState("");
  const ITEMS_PER_PAGE = 10;
  const { currentPage, currentItems, handlePageClick } = usePagination(
    quizzesLength,
    itemsPerPage
  );
  useEffect(() => {
    const fetchData = async () => {
      const data = { nickname: session?.nickname };
      const result = await getUserClearQuizzes(data);
      setQuizzes(result);
      setQuizzesLength(result?.puzzleTitle || []); // puzzleTitle이 undefined인 경우 빈 배열을 설정
    };

    fetchData();
  }, []);
  useEffect(() => {
    const updateItemsPerPage = () => {
      setItemsPerPage(window.innerWidth <= 700 ? 5 : 10);
    };

    window.addEventListener("resize", updateItemsPerPage);
    updateItemsPerPage();

    return () => window.removeEventListener("resize", updateItemsPerPage);
  }, []);

  // ...
  const totalPages = Math.ceil(quizzesLength.length / itemsPerPage);
  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) {
      return;
    }
  };

  useEffect(() => {
    const signUp = new Date(dummy2.users[0].signUpDate);
    const current = new Date("2024-01-30");

    const differenceInTime = current.getTime() - signUp.getTime();
    const differenceInDays = differenceInTime / (1000 * 3600 * 24);

    setDays(Math.ceil(differenceInDays));
    setLoading(false);
  }, []);
  const fetchData = async () => {
    const res = await fetch("/getUserProfile", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });
    const data = await res.json();
  };
  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div className={styles.background}>
      <div className={styles.container}>
        <div className={styles.layout}>
          <PrivacyControlBox
            selectedItem={selectedItem}
            setSelectedItem={setSelectedItem}
          />
          <div className={styles.right_layout}>
            <WelcomeMessage session={session} />
            <div className={styles.infomation}>
              <SelectedItem
                session={session}
                selectedItem={selectedItem}
                dummy={dummy}
                dummy2={dummy2}
                handleFileChange={handleFileChange}
                currentItems={currentItems}
                totalPages={totalPages}
                currentPage={currentPage}
                handlePageClick={handlePageClick}
                quizzes={quizzes}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Mypage;
