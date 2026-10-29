DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'transaction_type_check'
          AND conrelid = 'public.transaction'::regclass
    ) THEN
        ALTER TABLE public.transaction
            ADD CONSTRAINT transaction_type_check
            CHECK (type IN ('RECEITA', 'DESPESA'));
    END IF;
END $$;
