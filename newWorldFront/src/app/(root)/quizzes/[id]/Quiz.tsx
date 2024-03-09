"use client";
import React, { useEffect, useState } from "react";
import { postCheckQuizAPI } from "@/app/lib/api/quizzes";
import Header from "@/app/components/header/page";
import styles from "@/app/assets/scss/section/_quizPage.module.scss";
import { useRouter } from "next/navigation";
const Quiz = ({ quiz, quizId, nickname }: any) => {
  const router = useRouter();
  const [answer, setAnswer] = useState("");
  const [hintIndex, setHintIndex] = useState(-1);
  if (!quiz || !Array.isArray(quiz.hints)) {
    console.error("quiz or quiz.hints is not an array:", quiz);
    return null;
  }
  console.log(quiz);
  const checkAnswer = async () => {
    const data = {
      quizId: quizId,
      answer: answer,
      nickname: nickname,
    };
    console.log(data);
    const response = await postCheckQuizAPI(data);
    if (response?.data === "success") {
      console.log(response);
      alert("정답입니다!");
      router.push("/quizzes");
    } else {
      console.log(response);
      alert("틀렸습니다!");
      if (hintIndex < quiz.hints.length - 1) {
        setHintIndex(hintIndex + 1);
      }
    }
  };
  return (
    <>
      <Header></Header>
      <div className={styles.background}>
        <div className={styles.quiz_contents_box}>
          <h1 className={styles.quiz_title}>{quiz.quizTitle}</h1>
          <p className={styles.alert}>
            다른 사용자와 퀴즈 해결 과정이나 답안을 공유하지 말아주세요.
            <br />
            퀴즈를 틀렸을 때 나머지 힌트가 나타납니다!
          </p>
          <div className={styles.quiz_box}>
            <div className={styles.quiz_detail}>
              <p>{quiz.quizDetail}</p>
            </div>
            {/* <p>Answer: {quiz.answer}</p> */}
            <input
              className={styles.answer_input}
              type="text"
              onChange={(e) => {
                setAnswer(e.target.value);
              }}
            />
            <div>
              <div className={styles.hint_box}>
                {quiz.hints
                  .slice(0, hintIndex + 1)
                  .map((hint: { hint: string }, index: number) => (
                    <div
                      key={index}
                      className={index <= hintIndex ? "active" : ""} // 힌트가 활성화될 때만 'active' 클래스를 추가
                    >
                      {hint.hint}
                    </div>
                  ))}
              </div>
            </div>
            <button
              className={styles.button}
              type="submit"
              onClick={checkAnswer}
            >
              제출
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default Quiz;
