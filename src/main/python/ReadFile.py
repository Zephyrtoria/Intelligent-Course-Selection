import csv
import sqlite3
import mysql.connector

# 数据库连接信息
db_config = {
    'host': '127.0.0.1',
    'port': 3306,
    'user': 'root',
    'password': '123456',
    'database': 'intelligent-course-system'
}

# 定义 CSV 文件路径
courses_csv_file_path = "courses.csv"
course_period_csv_file_path = "course_period.csv"
prereq_csv_file_path = "prereq.csv"
succeed_csv_file_path = "succeed.csv"


# 连接 SQLite 数据库
def create_connection(db_config):
    try:
        # 连接数据库
        conn = mysql.connector.connect(**db_config)
        return conn

    except mysql.connector.Error as e:
        print("Error connecting to MySQL database:", e)


# 读取 CSV 文件并插入数据到数据库
def read_courses(conn, csv_file):
    try:
        cursor = conn.cursor(prepared=True)
        # 打开 CSV 文件
        with open(csv_file, 'r', encoding='utf-8') as file:
            reader = csv.reader(file)
            headers = next(reader)  # 读取表头，跳过表头行
            print("CSV Headers:", headers)
            # 插入数据
            for row in reader:
                insert_sql = "INSERT INTO course (course_basic_id, course_sp_id, course_name, teacher, department, semester, category, credit, beg_week, last_week, limits) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                cursor.execute(insert_sql, (
                    row[0], row[1], row[2], row[3], row[4], row[5], row[6], float(row[7]), int(row[8]),
                    int(row[9]), int(row[10])))
        # 提交事务
        conn.commit()
        print("Data inserted successfully")
    except sqlite3.Error as e:
        print(e)
    except Exception as ex:
        print(f"Error reading CSV file: {ex}")


def read_course_period(conn, csv_file):
    try:
        cursor = conn.cursor(prepared=True)
        # 打开 CSV 文件
        with open(csv_file, 'r', encoding='utf-8') as file:
            reader = csv.reader(file)
            headers = next(reader)  # 读取表头，跳过表头行
            print("CSV Headers:", headers)
            # 插入数据
            for row in reader:
                insert_sql = "INSERT INTO course_period (course_basic_id, course_sp_id, day, beg, last) VALUES(?, ?, ?, ?, ?) "
                cursor.execute(insert_sql, (
                    row[0], row[1], row[2], int(row[3]), int(row[4])))
        # 提交事务
        conn.commit()
        print("Data inserted successfully")
    except sqlite3.Error as e:
        print(e)
    except Exception as ex:
        print(f"Error reading CSV file: {ex}")


def read_prereq(conn, csv_file):
    try:
        cursor = conn.cursor(prepared=True)
        # 打开 CSV 文件
        with open(csv_file, 'r', encoding='utf-8') as file:
            reader = csv.reader(file)
            headers = next(reader)  # 读取表头，跳过表头行
            print("CSV Headers:", headers)
            # 插入数据
            for row in reader:
                insert_sql = "INSERT INTO prereq (course_basic_id, prereq_id) VALUES(?, ?) "
                cursor.execute(insert_sql, (row[0], row[1]))
        # 提交事务
        conn.commit()
        print("Data inserted successfully")
    except sqlite3.Error as e:
        print(e)
    except Exception as ex:
        print(f"Error reading CSV file: {ex}")


def read_succeed(conn, csv_file):
    try:
        cursor = conn.cursor(prepared=True)
        # 打开 CSV 文件
        with open(csv_file, 'r', encoding='utf-8') as file:
            reader = csv.reader(file)
            headers = next(reader)  # 读取表头，跳过表头行
            print("CSV Headers:", headers)
            # 插入数据
            for row in reader:
                insert_sql = "INSERT INTO succeed (course_basic_id, succeed_course_basic_id) VALUES(?, ?) "
                cursor.execute(insert_sql, (row[0], row[1]))
        # 提交事务
        conn.commit()
        print("Data inserted successfully")
    except sqlite3.Error as e:
        print(e)
    except Exception as ex:
        print(f"Error reading CSV file: {ex}")


# 主函数
def main():
    # 创建数据库连接
    conn = create_connection(db_config)
    if conn is not None:
        # 插入数据
        # read_courses(conn, courses_csv_file_path)
        read_course_period(conn, course_period_csv_file_path)
        read_prereq(conn, prereq_csv_file_path)
        read_succeed(conn, succeed_csv_file_path)
        # 关闭数据库连接
        conn.close()
    else:
        print("Error! Cannot connect to the database.")


if __name__ == "__main__":
    main()
