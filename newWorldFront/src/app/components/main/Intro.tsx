"use client";
import React, { useEffect, useState } from "react";
import styles from "./intro.module.scss";
import Playbutton from "./Playbutton";
import Link from "next/link";
import { Element } from "react-scroll";

import Footer from "@/app/components/footer/page";
import Header from "@/app/components/header/page";
import { useSession } from "next-auth/react";
const Intro = () => {
  const [lines, setLines] = useState([
    '"당신의 동화 같은 이야기를 기다리고 있어요."',
    "여기, 우리는 모두 작은 작가이자 독자이기도 한 곳입니다.",
    "당신의 상상력과 이야기를 우리와 함께 나누어보세요.",
    "특별한 순간을 만들어내는데 참여하실 수 있습니다.",
  ]);
  // const { key, startTyping } = useScrollToContainer();
  // const { currentLines, typingDone } = useTypingAnimation(startTyping, lines);
  const { data: session } = useSession();
  return (
    <div>
      <Header />
      <Element name="container1">
        <div className={styles.container}>
          <div className={styles.flexbox}>
            <div className={styles.main_box}>
              <div className={styles.contents}>
                <div className={styles.title}>
                  <div className={styles.title_text}>
                    <div>
                      <p>붉은 망토 ,</p>
                      <p>검은 그림자</p>
                    </div>
                    <div>
                      <span>{'"upon the final day of the year 1999"'}</span>
                    </div>
                    <div className={styles.play}>
                      <Link href={`/labyrinth`}>
                        <Playbutton></Playbutton>
                      </Link>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </Element>
      <Element name="container2">
        <div>
          <div className={styles.main_box2}>
            <div className={styles.container}>
              <div className={styles.contents_box}>
                <div className={styles.box1}></div>
                <div className={styles.box2}>
                  <div className={styles.box2_header}>
                    <div>Notice from @minimalmocha</div>
                    <div className={styles.follow}>Follow on Facebook</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </Element>
      <Element name="container3">
        <div className={styles.container3}>
          <div className={styles.main_box3}>
            <div className={styles.main_box3_container}>
              <div className={styles.flex1}></div>
            </div>
          </div>
        </div>
      </Element>
      <Footer />
    </div>
  );
};

export default Intro;
